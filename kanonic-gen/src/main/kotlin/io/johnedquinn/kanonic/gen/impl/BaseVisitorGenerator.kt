package io.johnedquinn.kanonic.gen.impl

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import io.johnedquinn.kanonic.gen.GrammarSpec

internal object BaseVisitorGenerator {

    /**
     * Creates the [FileSpec] containing the Grammar Node and its variants (Rule Nodes and the
     * Rule Variant Nodes).
     */
    public fun generate(grammar: GrammarSpec): FileSpec {
        val type = Private.generateType(grammar)
        return FileSpec.builder(grammar.packageName, grammar.baseVisitorName).also { file ->
            file.addType(type)
        }.build()
    }

    private object Private {
        fun defaultVisit(grammar: GrammarSpec) = FunSpec.builder("defaultVisit")
            .addModifiers(KModifier.OPEN)
            .addParameter(ParameterSpec("node", grammar.className))
            .addParameter(ParameterSpec("ctx", TypeVariableName("C")))
            .returns(TypeVariableName("R"))
            .beginControlFlow("for (child in node.children)")
            .addStatement("child.accept(this, ctx)")
            .endControlFlow()
            .addStatement("return defaultReturn(node, ctx)")
            .build()

        fun generateType(grammar: GrammarSpec): TypeSpec {
            val type = TypeSpec.classBuilder(grammar.baseVisitorName)
            type.addSuperinterface(grammar.visitorClassName.parameterizedBy(TypeVariableName("R"), TypeVariableName("C")))
            type.addTypeVariable(TypeVariableName("R"))
            type.addTypeVariable(TypeVariableName("C"))
            type.addDefaultReturn(grammar)
            type.addDefaultVisit(grammar)
            type.addVisitNode(grammar)
            type.addVisits(grammar)
            type.addModifiers(KModifier.ABSTRACT)
            return type.build()
        }

        fun TypeSpec.Builder.addVisitNode(grammar: GrammarSpec) = this.apply {
            val function = FunSpec.builder("visit")
            function.returns(TypeVariableName("R"))
            function.addParameter("node", ClassNames.NODE)
            function.addParameter("ctx", TypeVariableName("C"))
            function.addModifiers(KModifier.OPEN, KModifier.OVERRIDE)
            function.beginControlFlow("return when (node)")
            function.addStatement("is %T -> defaultVisit(node, ctx)", grammar.className)
            function.addStatement("else -> node.accept(this, ctx)")
            function.endControlFlow()
            this.addFunction(function.build())
        }

        fun TypeSpec.Builder.addDefaultReturn(grammar: GrammarSpec) = this.apply {
            val function = FunSpec.builder("defaultReturn")
            function.returns(TypeVariableName("R"))
            function.addParameter("node", grammar.className)
            function.addParameter("ctx", TypeVariableName("C"))
            function.addModifiers(KModifier.ABSTRACT)
            this.addFunction(function.build())
        }

        fun TypeSpec.Builder.addDefaultVisit(grammar: GrammarSpec) = this.apply {
            this.addFunction(defaultVisit(grammar))
        }

        fun TypeSpec.Builder.addVisits(grammar: GrammarSpec) = this.apply {
            grammar.rules.forEach { rule ->
                rule.variants.forEach { variant ->
                    val function = FunSpec.builder(variant.visitMethodName)
                    function.returns(TypeVariableName("R"))
                    function.addParameter("node", variant.className)
                    function.addParameter("ctx", TypeVariableName("C"))
                    function.addModifiers(KModifier.OPEN, KModifier.OVERRIDE)
                    function.addCode(CodeBlock.of("return defaultVisit(node, ctx)"))
                    this.addFunction(function.build())
                }

                val function = FunSpec.builder(rule.visitMethodName)
                function.returns(TypeVariableName("R"))
                function.addParameter("node", rule.className)
                function.addParameter("ctx", TypeVariableName("C"))
                function.addModifiers(KModifier.OPEN, KModifier.OVERRIDE)
                // function.addCode(CodeBlock.of("return defaultVisit(node, ctx)"))
                function.apply {
                    this.beginControlFlow("return when (node)")
                    rule.variants.forEach {
                        this.addStatement("is %T -> %L(node, ctx)", it.className, it.visitMethodName)
                    }
                    this.endControlFlow()
                }

                this.addFunction(function.build())
            }
        }
    }
}
