package com.actiangent.cuacagempa.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "province",
)
data class ProvinceEntity(
    @PrimaryKey
    val id: Long,

    val latitude: Double,

    val longitude: Double,

    val endpoint: String,
)

@Entity(
    tableName = "province_l10n_id",
    foreignKeys = [
        ForeignKey(
            entity = ProvinceEntity::class,
            parentColumns = ["id"],
            childColumns = ["province_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
internal data class ProvinceL10nIdEntity(
    @PrimaryKey
    val id: Long,

    val name: String,

    @ColumnInfo(name = "province_id")
    val provinceId: Long,
)

@Entity(
    tableName = "province_l10n_en",
    foreignKeys = [
        ForeignKey(
            entity = ProvinceEntity::class,
            parentColumns = ["id"],
            childColumns = ["province_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
internal data class ProvinceL10nEnEntity(
    @PrimaryKey
    val id: Long,

    val name: String,

    @ColumnInfo(name = "province_id")
    val provinceId: Long,
)

@Entity(
    tableName = "regency",
    foreignKeys = [
        ForeignKey(
            entity = ProvinceEntity::class,
            parentColumns = ["id"],
            childColumns = ["province_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
)
data class RegencyEntity(
    @PrimaryKey
    val id: String,

    val latitude: Double,

    val longitude: Double,

    @ColumnInfo(name = "province_id")
    val provinceId: Long,
)

@Entity(
    tableName = "regency_l10n_id",
    foreignKeys = [
        ForeignKey(
            entity = RegencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["regency_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
internal data class RegencyL10nIdEntity(
    @PrimaryKey
    val id: Long,

    val name: String,

    @ColumnInfo(name = "regency_id")
    val regencyId: String,
)

@Entity(
    tableName = "regency_l10n_en",
    foreignKeys = [
        ForeignKey(
            entity = RegencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["regency_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
internal data class RegencyL10nEnEntity(
    @PrimaryKey
    val id: Long,

    val name: String,

    @ColumnInfo(name = "regency_id")
    val regencyId: String,
)
