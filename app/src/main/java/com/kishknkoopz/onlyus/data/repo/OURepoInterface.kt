package com.kishknkoopz.onlyus.data.repo

import com.kishknkoopz.onlyus.data.models.OUActions
import com.kishknkoopz.onlyus.data.models.OUNormalGameInfo
import com.kishknkoopz.onlyus.data.models.OUUserConfig

interface OURepoInterface {

    fun getDares(userConfig: OUUserConfig, partners: OUNormalGameInfo): OUActions.OUDare

}