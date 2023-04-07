package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import io.johnedquinn.kanonic.runtime.grammar.RuleReference
import io.johnedquinn.kanonic.runtime.grammar.TokenDefinition
import io.johnedquinn.kanonic.runtime.parse.ParseTable

internal class GrammarSpec(
    val grammarName: String,
    val metadataName: String,
    val nodeName: String,
    val visitorName: String,
    val baseVisitorName: String,
    val packageName: String,
    val rules: List<RuleSpec>,
    val start: RuleReference,
    val tokens: List<TokenDefinition>,
    val className: ClassName,
    val visitorClassName: ClassName,
    val table: ParseTable
)
