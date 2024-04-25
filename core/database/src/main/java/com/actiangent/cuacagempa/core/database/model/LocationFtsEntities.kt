package com.actiangent.cuacagempa.core.database.model

import androidx.room.Entity
import androidx.room.Fts4

// ISO 639

@Fts4(contentEntity = ProvinceL10nIdEntity::class)
@Entity(tableName = "province_l10n_id_fts")
internal data class ProvinceL10nIdFtsEntity(

    val name: String
)

@Fts4(contentEntity = ProvinceL10nEnEntity::class)
@Entity(tableName = "province_l10n_en_fts")
internal data class ProvinceL10nEnFtsEntity(

    val name: String
)

@Fts4(contentEntity = RegencyL10nIdEntity::class)
@Entity(tableName = "regency_l10n_id_fts")
internal data class RegencyL10nIdFtsEntity(

    val name: String
)

@Fts4(contentEntity = RegencyL10nEnEntity::class)
@Entity(tableName = "regency_l10n_en_fts")
internal data class RegencyL10nEnFtsEntity(

    val name: String
)