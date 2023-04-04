package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import io.johnedquinn.kanonic.SymbolReference

class VariantSpec(
    val originalName: String,
    val name: String,
    val visitMethodName: String,
    val items: List<SymbolReference>,
    val implicitItems: List<SymbolReference>,
    val className: ClassName,
    val generated: Boolean
)
