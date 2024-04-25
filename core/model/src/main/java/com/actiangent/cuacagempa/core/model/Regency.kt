package com.actiangent.cuacagempa.core.model

data class Regency(
    val id: String,
    val name: String,
    val province: Province
) {
    companion object {

        fun shell(id: String) = Regency(
            id = id,
            name = "",
            province = Province(
                id = 0,
                name = ""
            )
        )

        fun empty() = Regency(
            id = "",
            name = "",
            province = Province(
                id = 0,
                name = ""
            )
        )
    }
}