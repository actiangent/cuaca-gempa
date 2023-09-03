package com.actiangent.cuacagempa.core.designsystem.component.graph

data class GraphEntry(
    val value: Number,
    val label: String = value.toString()
)

fun graphEntryOf(vararg entries: Number): List<GraphEntry> =
    entries.map { value -> GraphEntry(value) }

fun graphEntryOf(vararg entries: Pair<Number, String>): List<GraphEntry> =
    entries.map { (value, label) -> GraphEntry(value, label) }
