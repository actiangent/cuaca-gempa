package com.actiangent.cuacagempa.core.data.repository.earthquake

import android.util.Log
import com.actiangent.cuacagempa.core.common.result.Result
import com.actiangent.cuacagempa.core.common.result.asResult
import com.actiangent.cuacagempa.core.data.EarthquakeSynchronizer
import com.actiangent.cuacagempa.core.data.changeEarthquakeSync
import com.actiangent.cuacagempa.core.data.model.asEarthquakeEntity
import com.actiangent.cuacagempa.core.database.dao.EarthquakeDao
import com.actiangent.cuacagempa.core.database.dao.RegencyDao
import com.actiangent.cuacagempa.core.database.model.asRegency
import com.actiangent.cuacagempa.core.model.DetailedEarthquake
import com.actiangent.cuacagempa.core.network.RemoteEarthquakeDataSource
import com.actiangent.cuacagempa.core.network.model.NetworkEarthquake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultEarthquakeRepository @Inject constructor(
    private val regencyDao: RegencyDao,
    private val earthquakeDao: EarthquakeDao,
    private val remoteEarthquakeDataSource: RemoteEarthquakeDataSource
) : EarthquakeRepository {

    override fun getRecentEarthquake(): Flow<Result<DetailedEarthquake>> =
        earthquakeDao.getRecentEarthQuake()
            .map { earthquake ->
                Log.d("earthquake", "getRecentEarthquake: $earthquake")
                val regency = regencyDao.getOneOffRegencyByNearestLatLon(
                    earthquake.latitude,
                    earthquake.longitude
                ).asRegency()

                DetailedEarthquake(
                    earthquake = earthquake,
                    regency = regency,
                )
            }
            .asResult()

    override fun getLatestCautiousEarthquakes(): Flow<Result<List<DetailedEarthquake>>> =
        earthquakeDao.getLatestCautiousEarthquakes()
            .map { earthquakes ->
                earthquakes.map { earthquake ->
                    val regency = regencyDao.getOneOffRegencyByNearestLatLon(
                        earthquake.latitude,
                        earthquake.longitude
                    ).asRegency()

                    DetailedEarthquake(
                        earthquake = earthquake,
                        regency = regency,
                    )
                }
            }
            .asResult()

    override suspend fun syncWith(synchronizer: EarthquakeSynchronizer) =
        synchronizer.changeEarthquakeSync(
            earthquakeFetcher = {
                listOf(remoteEarthquakeDataSource.getRecentEarthQuake()) +
                        remoteEarthquakeDataSource.getLatestCautiousEarthquakes()
            },
            modelInserter = { networkEarthquakes ->
                val earthquakeEntities = networkEarthquakes
                    .map(NetworkEarthquake::asEarthquakeEntity)
                earthquakeDao.insertEarthquakes(earthquakeEntities)
            },
            modelDeleter = {
                earthquakeDao.deleteOldEarthquakes()
            },
        )
}