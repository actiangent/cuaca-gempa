package com.actiangent.cuacagempa.core.model

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK;

    override fun toString(): String {
        // capitalize each word
        return name
            .split("_")
            .joinToString(" ") { word ->
                val range = 1 until word.length
                word.replaceRange(
                    range = range,
                    replacement = word.substring(range).lowercase()
                )
            }
    }
}
