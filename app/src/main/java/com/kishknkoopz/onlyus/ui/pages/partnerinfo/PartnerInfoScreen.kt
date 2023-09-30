package com.kishknkoopz.onlyus.ui.pages.partnerinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.kishknkoopz.onlyus.ui.activity.OUMainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerInfoScreen(mainViewModel: OUMainViewModel) {
    val gameStartCoroutine = rememberCoroutineScope()
    val partnerOneName = remember {
        mutableStateOf("Nikhil")
    }
    val partnerTwoName = remember {
        mutableStateOf("Zebin")
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(all = 16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            TextField(value = partnerOneName.value,
                onValueChange = {
                    partnerOneName.value = it
                                },
                label = { Text(text = "Bae") }
            )
            TextField(value = partnerTwoName.value,
                onValueChange = {
                    partnerTwoName.value = it
                },
                label = { Text(text = "Boo") }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                    mainViewModel.onPlayerNamesAdded(
                        playerName = partnerOneName.value,
                        playerNameTwo = partnerTwoName.value
                    )
            }) {
                Text(text = "Go")
            }
        }
    }

}