package io.johnedquinn.kanonic.tool.pico

import io.johnedquinn.kanonic.gen.KanonicGenerator
import io.johnedquinn.kanonic.gen.impl.KanonicGrammar
import io.johnedquinn.kanonic.generated.KanonicMetadata
import io.johnedquinn.kanonic.parse.KanonicParser
import picocli.CommandLine
import java.io.File

@CommandLine.Command(
    name = "kanonic",
    mixinStandardHelpOptions = true,
    version = ["0.0.1"],
    descriptionHeading = "%n@|bold,underline,yellow Kanonic|@%n",
    description = [
        "Kanonic Parser Generator"
    ],
    showDefaultValues = true
)
internal class GenerateCommand : Runnable {

    @CommandLine.Parameters(arity = "1", index = "0", description = ["The Kanonic grammar file."], paramLabel = "KANONIC_FILE")
    var file: File? = null

    override fun run() {
        val fileContent = file?.reader()?.readText() ?: error("Unable to read file content. Exiting.")
        println(fileContent)
        val parser = KanonicParser.Builder
            .standard()
            .withGrammar(KanonicGrammar.grammar)
            .withMetadata(KanonicMetadata())
            .build()
        val ast = parser.parse(fileContent)
        println("AST: $ast")
        val grammar = KanonicGenerator.generate(TODO())
    }
}
