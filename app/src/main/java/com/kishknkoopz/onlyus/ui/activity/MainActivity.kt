package com.kishknkoopz.onlyus.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kishknkoopz.onlyus.ui.pages.dare.DareScreen
import com.kishknkoopz.onlyus.ui.pages.partnerinfo.PartnerInfoScreen
import com.kishknkoopz.onlyus.ui.states.OUMainState
import com.kishknkoopz.onlyus.ui.theme.OnlyUsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainViewModel: OUMainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlyUsTheme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "FirstScreen") {
                        composable("FirstScreen") {
                            MainScreen(navigation = navController, mainViewModel)
                        }
                        composable("SecondScreen") {
                            //SecondScreen(navigation = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navigation: NavController, mainViewModel: OUMainViewModel) {
    val state = mainViewModel.mainState.collectAsState()
    when(state.value) {
        is OUMainState.InitialState -> {
            PartnerInfoScreen(mainViewModel)
        }
        is OUMainState.DareState -> {
            DareScreen((state.value as OUMainState.DareState).gameState, mainViewModel)
        }
        else -> {
            PartnerInfoScreen(mainViewModel)
        }
    }
}
