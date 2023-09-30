package com.kishknkoopz.onlyus.data.repo

import com.kishknkoopz.onlyus.data.models.OUActions
import com.kishknkoopz.onlyus.data.models.OUDareBaseModel
import com.kishknkoopz.onlyus.data.models.OUNormalGameInfo
import com.kishknkoopz.onlyus.data.models.OUUserConfig
import com.kishknkoopz.onlyus.data.source.OULocalDareSource

class OURepo(private val localSource: OULocalDareSource,
    ) : OURepoInterface {

    override fun getDares(userConfig: OUUserConfig, partners: OUNormalGameInfo): OUActions.OUDare {
        return OUActions.OUDare(dareBase = OUDareBaseModel(localSource.getRandomDare(userConfig)), partners = partners)
    }
}