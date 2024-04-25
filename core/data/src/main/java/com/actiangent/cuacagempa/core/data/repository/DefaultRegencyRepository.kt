package com.actiangent.cuacagempa.core.data.repository

import com.actiangent.cuacagempa.core.database.dao.ProvinceDao
import com.actiangent.cuacagempa.core.database.dao.ProvinceFtsDao
import com.actiangent.cuacagempa.core.database.dao.RegencyDao
import com.actiangent.cuacagempa.core.database.dao.RegencyFtsDao
import com.actiangent.cuacagempa.core.database.model.RawRegency
import com.actiangent.cuacagempa.core.database.model.asRegency
import com.actiangent.cuacagempa.core.model.Regency
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultRegencyRepository @Inject constructor(
    private val provinceDao: ProvinceDao,
    private val regencyDao: RegencyDao,
    private val provinceFtsDao: ProvinceFtsDao,
    private val regencyFtsDao: RegencyFtsDao,
) : RegencyRepository {

    override fun getRegency(id: String): Flow<Regency> =
        regencyDao.getRegency(id).map(RawRegency::asRegency)

    override fun getRegencies(ids: Set<String>): Flow<List<Regency>> =
        regencyDao.getRegencies(ids).map { regencies -> regencies.map(RawRegency::asRegency) }

    override fun searchAllRegencies(query: String): Flow<List<Regency>> {
        val provinceRowIds = provinceFtsDao.searchAllProvinces("%$query%")
//        val regencyRowIds = regencyFtsDao.searchAllRegencies("%$query%")

        val provinceIds = provinceRowIds
            .mapLatest { it.toSet() }
            .distinctUntilChanged()
            .flatMapLatest(provinceDao::getProvinces)
            .map { provinces ->
                provinces.map { it.id }
            }

        val rr2 = provinceIds
            .mapLatest { it.toSet() }
            .distinctUntilChanged()
            .flatMapLatest(regencyDao::getRegenciesByProvinceIds)

        val regencies = regencyDao.searchAllRegencies("%$query%")
            .combine(rr2) { r1, r2 -> r1 + r2 }
            .map { rawRegencies ->
                rawRegencies.toSet().map(RawRegency::asRegency)
            }

        return regencies
    }
}