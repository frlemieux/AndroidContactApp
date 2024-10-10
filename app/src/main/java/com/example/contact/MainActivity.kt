package com.example.contact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contact.ui.theme.ContactTheme
import com.example.ui.details.DetailsScreen
import com.example.ui.list.ListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ContactTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Destination.Contacts,
                        ) {
                            composable<Destination.Contacts> {
                                ListScreen(
                                    onItemClicked = {
                                        navController.navigate(Destination.Details(it))
                                    },
                                    modifier = Modifier.padding(innerPadding),
                                )
                            }
                            composable<Destination.Details> {
                                DetailsScreen(
                                    onBackPressed = { navController.navigateUp() },
                                    modifier = Modifier.padding(innerPadding),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed interface Destination {
    @Serializable
    data object Contacts : Destination

    @Serializable
    data class Details(
        val index: Int,
    ) : Destination
}
