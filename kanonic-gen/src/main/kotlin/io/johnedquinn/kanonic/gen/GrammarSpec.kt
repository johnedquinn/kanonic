package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.ClassName
import io.johnedquinn.kanonic.RuleReference
import io.johnedquinn.kanonic.parse.TokenDefinition
public class GrammarSpec(
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
    val visitorClassName: ClassName
)
