package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import io.johnedquinn.kanonic.runtime.grammar.SymbolReference

internal class VariantSpec(
    val originalName: String,
    val name: String,
    val visitMethodName: String,
    val items: List<SymbolReference>,
    val implicitItems: List<SymbolReference>,
    val className: ClassName,
    val generated: Boolean
)
