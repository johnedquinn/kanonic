package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName

class RuleSpec(
    val name: String,
    val visitMethodName: String,
    val variants: List<VariantSpec>,
    val className: ClassName
)
