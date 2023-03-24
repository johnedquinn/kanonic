package io.johnedquinn.kanonic.gen

import com.squareup.kotlinpoet.FileSpec
import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.gen.impl.NodeGenerator
import io.johnedquinn.kanonic.gen.impl.MetadataGenerator

public object KanonicGenerator {

    /**
     * Generates all files
     */
    public fun generate(grammar: Grammar): List<FileSpec> = listOf(
        MetadataGenerator.generate(grammar),
        NodeGenerator.generate(grammar)
    )
}
