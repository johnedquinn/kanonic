package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.ClassName
import io.johnedquinn.kanonic.runtime.grammar.Grammar
import java.util.Locale

internal object GrammarUtils {

    public fun getPackageName(grammar: Grammar): String = grammar.options.packageName ?: "io.johnedquinn.kanonic.example"

    public fun getMetadataName(grammar: Grammar): String = "${grammar.options.grammarName}Specification"

    // Converts snake_case to UpperCamelCase
    public fun getNormalizedName(prefix: String): String {
        val pattern = "_[a-z]".toRegex()
        return prefix.replace(pattern) { it.value.last().uppercase() }
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    public fun getNormalizedCamelCaseName(prefix: String): String {
        val pattern = "_[a-z]".toRegex()
        return prefix.replace(pattern) { it.value.last().uppercase() }
    }

    public fun getGeneratedClassName(prefix: String): String {
        return "${getNormalizedName(prefix)}Node"
    }

    public fun getGeneratedVisitorName(grammar: Grammar) = "${grammar.options.grammarName}Visitor"

    public fun getGeneratedBaseVisitorName(grammar: Grammar) = "${grammar.options.grammarName}BaseVisitor"

    public fun getGrammarNodeName(grammar: Grammar) = getGeneratedClassName(grammar.options.grammarName)

    public fun getGeneratedNodeClassName(grammar: Grammar): ClassName {
        return ClassName(getPackageName(grammar), getGrammarNodeName(grammar))
    }
}
