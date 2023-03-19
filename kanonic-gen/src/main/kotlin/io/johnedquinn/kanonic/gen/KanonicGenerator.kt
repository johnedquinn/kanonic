package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.FileSpec
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.gen.impl.NodeGenerator
import io.johnedquinn.kanonic.gen.impl.ParserInfoGenerator

public object KanonicGenerator {

    /**
     * Generates all files
     */
    public fun generate(grammar: Grammar): List<FileSpec> = listOf(
        ParserInfoGenerator.generate(grammar),
        NodeGenerator.generate(grammar)
    )
}
