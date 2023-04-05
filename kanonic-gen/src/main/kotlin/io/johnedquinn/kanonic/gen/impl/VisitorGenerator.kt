package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
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
            type.addSuperinterface(ClassNames.NODE_VISITOR.parameterizedBy(TypeVariableName("R"), TypeVariableName("C")))
            type.addTypeVariable(TypeVariableName("R"))
            type.addTypeVariable(TypeVariableName("C"))
            type.addVisits(grammar)
            return type.build()
        }

        fun TypeSpec.Builder.addVisits(grammar: GrammarSpec) = this.apply {
            grammar.rules.forEach { rule ->
                if (rule.generated) {
                    return@forEach
                }
                rule.variants.forEach { variant ->
                    val funcName = variant.visitMethodName
                    val function = FunSpec.builder(funcName)
                    function.returns(TypeVariableName("R"))
                    function.addParameter("node", variant.className)
                    function.addParameter("ctx", TypeVariableName("C"))
                    function.addModifiers(KModifier.ABSTRACT)
                    this.addFunction(function.build())
                }

                val funcName = rule.visitMethodName
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
