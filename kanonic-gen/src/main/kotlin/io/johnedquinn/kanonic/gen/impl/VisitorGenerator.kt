package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import io.johnedquinn.kanonic.gen.GrammarSpec

internal object VisitorGenerator {

    /**
     * Creates the [FileSpec] containing the Grammar Node and its variants (Rule Nodes and the
     * Rule Variant Nodes).
     */
    public fun generate(grammar: GrammarSpec): FileSpec {
        val type = Private.generateType(grammar)
        return FileSpec.builder(grammar.packageName, grammar.visitorName).also { file ->
            file.addType(type)
        }.build()
    }

    private object Private {
        fun generateType(grammar: GrammarSpec): TypeSpec {
            val type = TypeSpec.interfaceBuilder(grammar.visitorName)
            type.addTypeVariable(TypeVariableName("R"))
            type.addTypeVariable(TypeVariableName("C"))
            type.addVisit(grammar)
            type.addVisits(grammar)
            return type.build()
        }

        fun TypeSpec.Builder.addVisit(grammar: GrammarSpec) = this.apply {
            val function = FunSpec.builder("visit")
            function.returns(TypeVariableName("R"))
            function.addParameter("node", grammar.className)
            function.addParameter("ctx", TypeVariableName("C"))
            function.addModifiers(KModifier.ABSTRACT)
            this.addFunction(function.build())
        }

        fun TypeSpec.Builder.addVisits(grammar: GrammarSpec) = this.apply {
            grammar.rules.forEach { rule ->
                rule.variants.forEach { variant ->
                    val funcName = "visit${variant.name}"
                    val function = FunSpec.builder(funcName)
                    function.returns(TypeVariableName("R"))
                    function.addParameter("node", variant.className)
                    function.addParameter("ctx", TypeVariableName("C"))
                    function.addModifiers(KModifier.ABSTRACT)
                    this.addFunction(function.build())
                }

                val funcName = "visit${rule.name}"
                val function = FunSpec.builder(funcName)
                function.returns(TypeVariableName("R"))
                function.addParameter("node", rule.className)
                function.addParameter("ctx", TypeVariableName("C"))
                function.addModifiers(KModifier.ABSTRACT)
                this.addFunction(function.build())
            }
        }
    }
}
