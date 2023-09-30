package com.kishknkoopz.onlyus.ui.activity

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kishknkoopz.onlyus.application.OUApplicationHelper
import com.kishknkoopz.onlyus.data.models.OUActions
import com.kishknkoopz.onlyus.data.models.OUNormalGameInfo
import com.kishknkoopz.onlyus.data.models.OUPartner
import com.kishknkoopz.onlyus.data.models.OUUserConfig
import com.kishknkoopz.onlyus.data.models.OUUserType
import com.kishknkoopz.onlyus.data.repo.OURepo
import com.kishknkoopz.onlyus.data.repo.OURepoInterface
import com.kishknkoopz.onlyus.data.source.OULocalDareSource
import com.kishknkoopz.onlyus.ui.states.OUGameData
import com.kishknkoopz.onlyus.ui.states.OUMainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OUMainViewModel : ViewModel() {

    val mainState = MutableStateFlow<OUMainState>((OUMainState.InitialState()))
    val mainRepo = OURepo(localSource = OULocalDareSource(context = OUApplicationHelper.getAppContext()))

    private val _dareFlow = MutableStateFlow<OUActions.OUDare?>(null)
    val dareFlow: StateFlow<OUActions.OUDare?> = _dareFlow

    fun onPlayerNamesAdded(playerName: String, playerNameTwo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainState.emit(
                OUMainState.DareState(
                    gameState = OUGameData(
                        userConfig = OUUserConfig(isLoggedIn = true, userType = OUUserType.Free),
                        partners = OUNormalGameInfo(
                            partnerOne = OUPartner(name = playerName),
                            partnerTwo = OUPartner(name = playerNameTwo),
                            currentTurn = 0
                        )
                    )
                )
            )
        }
    }

    fun getNextDare(userConfig: OUUserConfig, partners: OUNormalGameInfo) {
         val dare = mainRepo.getDares(userConfig, partners)
        _dareFlow.value = dare
    }


}