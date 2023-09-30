package com.kishknkoopz.onlyus.data.models

data class OUUserConfig(val isLoggedIn: Boolean, val userType: OUUserType)

sealed class OUUserType {
    object Free : OUUserType()
    object Premium : OUUserType()
}