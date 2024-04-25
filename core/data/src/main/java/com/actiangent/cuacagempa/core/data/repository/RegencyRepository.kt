package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.model.Regency
import kotlinx.coroutines.flow.Flow

interface RegencyRepository {

    fun getRegency(id: String): Flow<Regency>

    fun getRegencies(ids: Set<String>): Flow<List<Regency>>

    fun searchAllRegencies(query: String): Flow<List<Regency>>
}