package com.actiangent.cuacagempa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.actiangent.cuacagempa.core.database.model.ProvinceEntity
import com.actiangent.cuacagempa.core.database.model.RawRegency
import com.actiangent.cuacagempa.core.database.model.RegencyEntity
import com.actiangent.cuacagempa.core.model.Province
import kotlinx.coroutines.flow.Flow

@Dao
interface ProvinceDao {

    @Query(
        """
        SELECT
            province.id,
            province_l10n_en.name
        FROM
            province
        JOIN 
            province_l10n_en 
        ON 
            province.id == province_l10n_en.province_id
        WHERE
            province.id IN (:ids)
        LIMIT
            1
    """
    )
    fun getProvinces(ids: Set<Long>): Flow<List<Province>>

    @Query(
        """
        SELECT
            *
        FROM
            province
        WHERE
            id = :provinceId
        LIMIT
            1
    """
    )
    suspend fun getOneOffProvinceEntityById(provinceId: Long): ProvinceEntity

    @Query(
        """
        SELECT
            *
        FROM
            province
        WHERE
            id = (SELECT regency.province_id FROM regency WHERE regency.id = :regencyId LIMIT 1)
        LIMIT
            1
    """
    )
    suspend fun getOneOffProvinceEntityByRegencyId(regencyId: String): ProvinceEntity

    @Query(
        """
        SELECT
            *
        FROM
            province
        ORDER BY
            ABS(latitude - :latitude) + ABS(longitude - :longitude) ASC
        LIMIT
            1
    """
    )
    suspend fun getOneOffProvinceEntityByNearestLatLon(
        latitude: Double,
        longitude: Double
    ): ProvinceEntity
}

@Dao
interface RegencyDao {

    @Query(
        """
        SELECT 
            regency.id AS id, 
            regency_l10n_en.name AS name, 
            province.id AS provinceId, 
            province_l10n_en.name AS provinceName 
        FROM regency 
        JOIN regency_l10n_en ON regency.id == regency_l10n_en.regency_id 
        JOIN province ON regency.province_id == province.id
        JOIN province_l10n_en ON province.id == province_l10n_en.province_id
        WHERE
            regency.id IN (:ids)
    """
    )
    fun getRegencies(ids: Set<String>): Flow<List<RawRegency>>

    @Query(
        """
        SELECT 
            regency.id AS id, 
            regency_l10n_en.name AS name, 
            province.id AS provinceId, 
            province_l10n_en.name AS provinceName 
        FROM regency 
        JOIN regency_l10n_en ON regency.id == regency_l10n_en.regency_id 
        JOIN province ON regency.province_id == province.id
        JOIN province_l10n_en ON province.id == province_l10n_en.province_id
        WHERE
            regency.id = :regencyId
        LIMIT
            1
    """
    )
    fun getRegency(
        regencyId: String
    ): Flow<RawRegency>

    @Query(
        """
        SELECT 
            regency.id AS id, 
            regency_l10n_en.name AS name, 
            province.id AS provinceId, 
            province_l10n_en.name AS provinceName 
        FROM regency 
        JOIN regency_l10n_en ON regency.id == regency_l10n_en.regency_id 
        JOIN province ON regency.province_id == province.id
        JOIN province_l10n_en ON province.id == province_l10n_en.province_id
        WHERE 
            province.id IN (:ids);
    """
    )
    fun getRegenciesByProvinceIds(ids: Set<Long>): Flow<List<RawRegency>>

    @Query(
        """
        SELECT 
            regency.id AS id, 
            regency_l10n_en.name AS name, 
            province.id AS provinceId, 
            province_l10n_en.name AS provinceName 
        FROM regency 
        JOIN regency_l10n_en ON regency.id == regency_l10n_en.regency_id 
        JOIN province ON regency.province_id == province.id
        JOIN province_l10n_en ON province.id == province_l10n_en.province_id
        WHERE regency_l10n_en.name LIKE :query;
    """
    )
    fun searchAllRegencies(query: String): Flow<List<RawRegency>>

    @Query(
        """
        SELECT 
            regency.id AS id, 
            regency_l10n_en.name AS name, 
            province.id AS provinceId, 
            province_l10n_en.name AS provinceName 
        FROM regency 
        JOIN regency_l10n_en ON regency.id == regency_l10n_en.regency_id 
        JOIN province ON regency.province_id == province.id
        JOIN province_l10n_en ON province.id == province_l10n_en.province_id
        WHERE
            regency.id = :regencyId
        LIMIT
            1
    """
    )
    suspend fun getOneOffRegencyById(
        regencyId: String
    ): RawRegency

    @Query(
        """
        SELECT 
            regency.id AS id, 
            regency_l10n_en.name AS name, 
            province.id AS provinceId, 
            province_l10n_en.name AS provinceName 
        FROM regency 
        JOIN regency_l10n_en ON regency.id == regency_l10n_en.regency_id 
        JOIN province ON regency.province_id == province.id
        JOIN province_l10n_en ON province.id == province_l10n_en.province_id
        ORDER BY
            ABS(regency.latitude - :latitude) + ABS(regency.longitude - :longitude) ASC
        LIMIT
            1
    """
    )
    suspend fun getOneOffRegencyByNearestLatLon(
        latitude: Double,
        longitude: Double
    ): RawRegency

    @Query(
        """
        SELECT
            *
        FROM
            regency
        ORDER BY
            ABS(latitude - :latitude) + ABS(longitude - :longitude) ASC
        LIMIT
            1
    """
    )
    suspend fun getOneOffRegencyEntityByNearestLatLon(
        latitude: Double,
        longitude: Double
    ): RegencyEntity

    @Query(
        """
        SELECT
            *
        FROM
            regency
        WHERE
            province_id = :provinceId
        ORDER BY
            ABS(latitude - :latitude) + ABS(longitude - :longitude) ASC
        LIMIT
            1
    """
    )
    suspend fun getOneOffRegencyEntityByProvinceIdAndNearestLatLon(
        latitude: Double,
        longitude: Double,
        provinceId: Long
    ): RegencyEntity
}