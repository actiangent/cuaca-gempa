package com.actiangent.cuacagempa.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProvinceFtsDao {

    @Query("SELECT province_l10n_en.province_id FROM province_l10n_en JOIN province_l10n_en_fts ON province_l10n_en.id == province_l10n_en_fts.rowid WHERE province_l10n_en_fts.name LIKE :query")
    fun searchAllProvinces(query: String): Flow<List<Long>>
}

@Dao
interface RegencyFtsDao {

    @Query("SELECT rowid FROM regency_l10n_en_fts WHERE name LIKE :query")
    fun searchAllRegencies(query: String): Flow<List<Long>>
}