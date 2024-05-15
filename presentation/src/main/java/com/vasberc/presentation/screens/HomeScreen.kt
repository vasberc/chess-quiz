package com.vasberc.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.vasberc.presentation.navigation.ChessQuizNavHost
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {

    var message by remember { mutableStateOf<String?>(null) }

    val navController = rememberNavController()


    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        ChessQuizNavHost(
            modifier = Modifier.padding(paddingValues),
            navController =  navController,
            onMessage = { messageToShow ->
                message = messageToShow
            }
        )

        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(message) {
            coroutineScope.launch {
                if(message != null) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = message!!, duration = SnackbarDuration.Long)
                    message = null
                }
            }
        }
    }
}