package com.laioffer.netflix.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// Temporary Home UI that proves MVVM data flow works.
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchHomeFeed()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Netflix",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Home",
            style = MaterialTheme.typography.titleLarge
        )

        Text(text = "Architecture checkpoint")

        when (val state = uiState) {
            HomeUiState.Loading -> {
                Text(text = "Loading home feed...")
            }

            is HomeUiState.Error -> {
                Text(text = "Unable to load home feed: ${state.message}")
            }

            is HomeUiState.Success -> {
                Text(text = "Top recommendation: ${state.topRecommended?.title ?: "None"}")
                Text(text = "Sections loaded: ${state.sections.size}")
                Text(text = "Data flows through Hilt, HomeRepository, and HomeViewModel.")
            }
        }
    }
}