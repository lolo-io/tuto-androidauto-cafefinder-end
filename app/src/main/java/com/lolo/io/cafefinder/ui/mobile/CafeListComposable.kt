package com.lolo.io.cafefinder.ui.mobile

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lolo.io.cafefinder.domain.model.POI
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CafeListComposable(
    onItemClicked: (poi: POI) -> Unit,
    withUserLocation: (block: (Location?) -> Unit) -> Unit,
    allPermissionsGranted: Boolean
) {
    val viewModel = getViewModel<CafeListViewModel>()
    val state = viewModel.state

    val coroutineScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = coroutineScope.launch {
        withUserLocation {
            if (it != null) {
                viewModel.getNearestCafes(it)
            } else {
                refreshing = false
            }
        }
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    LaunchedEffect(allPermissionsGranted) {
        refresh()
    }

    refreshing = state.loading

    if (state.error != null) {
        Error(errorMessage = state.error)
    } else {
        Box(
            Modifier
                .fillMaxWidth()
                .pullRefresh(refreshState)
        ) {
            LazyColumn {
                items(state.pois) { poi ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemClicked(poi)
                            }) {
                        CafeRow(poi)
                    }
                }
            }
            PullRefreshIndicator(refreshing, refreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
fun CafeRow(poi: POI) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        Column(Modifier.padding(vertical = 8.dp, horizontal = 24.dp)) {
            Text(text = poi.name, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold))
            Text(
                text = poi.street ?: "unknown",
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                color = Color.DarkGray
            )
            Text(text = poi.type ?: "unknown", style = TextStyle(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun Error(errorMessage: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp), text = errorMessage,
        textAlign = TextAlign.Center
    )

}