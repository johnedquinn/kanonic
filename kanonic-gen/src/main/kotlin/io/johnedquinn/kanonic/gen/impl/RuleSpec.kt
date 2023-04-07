package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName

internal class RuleSpec(
    val name: String,
    val nodeName: String,
    val visitMethodName: String,
    val variants: List<VariantSpec>,
    val className: ClassName,
    val generated: Boolean
)
