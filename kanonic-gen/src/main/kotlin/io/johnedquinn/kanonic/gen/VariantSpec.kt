package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import io.johnedquinn.kanonic.SymbolReference

class VariantSpec(
    val name: String,
    val items: List<SymbolReference>,
    val className: ClassName
)
