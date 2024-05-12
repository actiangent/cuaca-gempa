package com.actiangent.cuacagempa.core.data.repository.earthquake

import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.data.EarthquakeSyncable
import com.actiangent.cuacagempa.core.model.DetailedEarthquake
import kotlinx.coroutines.flow.Flow

interface EarthquakeRepository : EarthquakeSyncable {

    fun getRecentEarthquake(): Flow<Result<DetailedEarthquake>>

    fun getLatestCautiousEarthquakes(): Flow<Result<List<DetailedEarthquake>>>
}