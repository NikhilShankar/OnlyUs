package com.kishknkoopz.onlyus.data.models

import com.kishknkoopz.onlyus.constants.OUConstants

data class OUDareBaseModel(val text: String)

sealed class OUActions :  OUActionInterface, OUGetGameInfoInterface{
    data class OUDare(private val dareBase : OUDareBaseModel, private val partners: OUNormalGameInfo) : OUActions(), OUActionInterface, OUGetGameInfoInterface by partners {
        override fun getDisplayText(): String {
            return dareBase.text
                .replace(OUConstants.StringPlaceholder.PLAYING_PARTNER_PLACEHOLDER,
                    partners.getPlayingPartnerName())
                .replace(OUConstants.StringPlaceholder.BAE_PARTNER_PLACEHOLDER,
                    partners.getBaePartnerName())

        }
    }
}

interface OUActionInterface {
    fun getDisplayText() : String

}

interface OUGetGameInfoInterface {
    fun getPlayingPartnerName() : String
    fun getBaePartnerName() : String
    fun next()
}

