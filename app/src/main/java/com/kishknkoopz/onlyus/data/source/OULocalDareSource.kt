package com.kishknkoopz.onlyus.data.source

import android.content.Context
import com.kishknkoopz.onlyus.R
import com.kishknkoopz.onlyus.data.models.OUUserConfig

class OULocalDareSource(context: Context) {

    var freeDareList: List<String> = listOf()

    init {
        freeDareList = context.resources.getStringArray(R.array.ou_free_dares).toList()
    }

    fun getRandomDare(userConfig: OUUserConfig) : String {
        return freeDareList.random()
    }

    fun getAllDares(userConfig: OUUserConfig) : List<String> {
        return freeDareList
    }


}