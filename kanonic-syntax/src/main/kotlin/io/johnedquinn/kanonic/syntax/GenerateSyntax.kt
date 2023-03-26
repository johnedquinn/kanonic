package io.johnedquinn.kanonic.syntax

import io.johnedquinn.kanonic.gen.KanonicGenerator
import picocli.CommandLine
import kotlin.io.path.Path

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

    override fun run() {
        val grammar = KanonicGrammar.grammar
        val files = KanonicGenerator.generate(grammar)
        files.forEach {
            val path = Path("kanonic-runtime/build/generated-src")
            it.writeTo(path)
        }
    }
}
