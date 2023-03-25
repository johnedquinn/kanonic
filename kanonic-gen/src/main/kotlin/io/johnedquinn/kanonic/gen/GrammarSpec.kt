package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName

public class GrammarSpec(
    val nodeName: String,
    val visitorName: String,
    val baseVisitorName: String,
    val packageName: String,
    val rules: List<RuleSpec>,
    val className: ClassName
)
