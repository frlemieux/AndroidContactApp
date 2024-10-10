package com.example.ui.details

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel>(),
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsState()
    Column {
        TopAppBar(
            title = { Text(text = "Details") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBackPressed()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            },
            modifier = modifier,
        )
        DetailsContent(
            state = state.value,
            modifier = modifier,
        )
    }
}

@Composable
fun DetailsContent(
    state: DetailsUiState,
    modifier: Modifier,
) {
    when (state) {
        is DetailsUiState.Loading -> LoadingScreen(modifier = modifier)
        is DetailsUiState.Error -> ErrorScreen(modifier = modifier)
        is DetailsUiState.Contact -> ContactScreen(contact = state.contact, modifier = modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "An error occurred. Please try again.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun ContactScreen(
    contact: ContactUi,
    modifier: Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollState(),
                ),
    ) {
        Text(
            text = contact.name,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Phone: ${contact.phone}",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Email: ${contact.email}",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Address: ${contact.address}",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
