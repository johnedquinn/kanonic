package io.johnedquinn.kanonic.syntax

import io.johnedquinn.kanonic.gen.KanonicGenerator
import picocli.CommandLine
import java.nio.file.Path

@CommandLine.Command(
    name = "kanonic-generate",
    mixinStandardHelpOptions = true,
    version = ["0.0.1"],
    descriptionHeading = "%n@|bold,underline,yellow Kanonic|@%n",
    description = [
        "Kanonic Parser Generator"
    ],
    showDefaultValues = true
)
internal class GenerateSyntax : Runnable {

    @CommandLine.Parameters(arity = "0..1", index = "0..1", description = ["The output directory"], paramLabel = "<directory>")
    lateinit var output: Path

    override fun run() {
        val grammar = KanonicGrammar.grammar
        val files = KanonicGenerator.generate(grammar)
        files.forEach {
            it.writeTo(output)
        }
    }
}
