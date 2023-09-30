package com.kishknkoopz.onlyus.data.models

data class OUNormalGameInfo(val partnerOne: OUPartner, val partnerTwo: OUPartner, var currentTurn: Int) : OUGetGameInfoInterface {
    override fun getPlayingPartnerName(): String {
        return if(currentTurn % 2 == 0) {
            partnerOne.name
        } else {
            partnerTwo.name
        }
    }

    override fun getBaePartnerName(): String {
        return if(currentTurn % 2 == 0) {
            partnerTwo.name
        } else {
            partnerOne.name
        }
    }

    override fun next() {
        currentTurn++
    }

}

data class OUPartner(val name: String)
