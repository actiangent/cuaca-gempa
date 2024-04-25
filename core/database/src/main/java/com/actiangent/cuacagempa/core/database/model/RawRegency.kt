package com.actiangent.cuacagempa.core.database.model

import com.actiangent.cuacagempa.core.model.Province
import com.actiangent.cuacagempa.core.model.Regency

data class RawRegency(
    val id: String,
    val name: String,
    val provinceId: Long,
    val provinceName: String
)

fun RawRegency.asRegency() = Regency(
    id = id,
    name = name,
    province = Province(
        id = provinceId,
        name = provinceName
    )
)