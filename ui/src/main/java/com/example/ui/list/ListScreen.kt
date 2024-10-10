package com.example.ui.list

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.ui.list.component.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel<ListViewModel>(),
    modifier: Modifier,
    onItemClicked: (Int) -> Unit,
) {
    val contacts = viewModel.pagingData.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    Column {
        ListContent(
            contacts = contacts,
            searchQuery = searchQuery,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onSearchTriggered = viewModel::onSearchTriggered,
            modifier = modifier,
            onItemClicked = onItemClicked,
        )
    }
}

@Composable
fun ListContent(
    contacts: LazyPagingItems<ContactUi>,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    modifier: Modifier,
    onItemClicked: (Int) -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        snapshotFlow { contacts.loadState }.collect { loadStates ->
            if (loadStates.refresh is LoadState.Error || loadStates.append is LoadState.Error) {
                Toast
                    .makeText(
                        context,
                        "No internet connection",
                        Toast.LENGTH_LONG,
                    ).show()
            }
        }
    }
    SearchTextField(
        onSearchQueryChanged = onSearchQueryChanged,
        onSearchTriggered = onSearchTriggered,
        searchQuery = searchQuery,
        modifier = modifier,
    )
    Box(modifier = modifier.fillMaxSize()) {
        if (contacts.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(items = contacts, key = { contact -> contact.index }) { contact ->
                    if (contact != null) {
                        ContactItem(
                            contact = contact,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemClicked(contact.index)
                                    },
                        )
                    }
                }
                item {
                    if (contacts.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: ContactUi,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.padding(start = 16.dp)) {
        AsyncImage(
            model = contact.image,
            contentDescription = null,
            modifier =
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .width(40.dp)
                    .height(40.dp),
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = contact.contactName)
            Text(text = contact.gender)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun ContactItemPreview() {
    ContactItem(
        contact =
            ContactUi(
                index = 0,
                contactName = "John",
                gender = "Male",
                capitalLetter = "J",
                capitalLetterBackgroundColor = Color.Red,
                image = "",
            ),
    )
}
