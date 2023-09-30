package com.kishknkoopz.onlyus.ui.states

import androidx.compose.runtime.Stable
import com.kishknkoopz.onlyus.data.models.OUPartner
import com.kishknkoopz.onlyus.data.models.OUNormalGameInfo
import com.kishknkoopz.onlyus.data.models.OUUserConfig
import com.kishknkoopz.onlyus.data.models.OUUserType

sealed class OUMainState {
    class InitialState : OUMainState()
    class DareState(val gameState: OUGameData) : OUMainState()
}


data class OUGameData(@Stable val userConfig: OUUserConfig,
                      @Stable val partners: OUNormalGameInfo,
                      @Stable val editPartners: Boolean = false
    ) {
    fun getInitialState(userConfig: OUUserConfig, partnerName: String, partnerName2: String) : OUGameData {
        return OUGameData(
            userConfig = userConfig,
            partners = OUNormalGameInfo(
                partnerOne = OUPartner(name = partnerName),
                partnerTwo = OUPartner(name = partnerName2),
                currentTurn = 0
            )
        )
    }

    fun toEditState() {
        this.copy(editPartners = true)
    }

    fun toStableState() {
        this.copy(editPartners = false)
    }

    fun editPlayerOne(partner: OUPartner) {
        this.copy(
            partners = this.partners.copy(
                partnerOne = partner,
                currentTurn = 0
            )
        )
    }

    fun editPlayerTwo(partner: OUPartner) {
        this.copy(
            partners = this.partners.copy(
                partnerTwo = partner,
                currentTurn = 0
            )
        )
    }

    fun canStartGame() : Boolean {
        return this.partners.getPlayingPartnerName().isNotEmpty() && this.partners.getBaePartnerName().isNotEmpty()
    }

}










