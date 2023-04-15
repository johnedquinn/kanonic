package io.johnedquinn.kanonic.syntax

import io.johnedquinn.kanonic.gen.KanonicGenerator
import io.johnedquinn.kanonic.runtime.grammar.TokenDefinition
import picocli.CommandLine
import java.nio.file.Path
import java.util.logging.Logger

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

    private val logger: Logger = Logger.getLogger("kanonic")

    @CommandLine.Parameters(arity = "0..1", index = "0..1", description = ["The output directory"], paramLabel = "<directory>")
    lateinit var output: Path

    override fun run() {
        val grammar = KanonicGrammar.grammar
        logger.fine(getTokenDefinitionsString(grammar.tokens))
        val files = KanonicGenerator.generate(grammar)
        files.forEach {
            it.writeTo(output)
        }
    }

    private fun getTokenDefinitionsString(tokens: List<TokenDefinition>): String = buildString {
        appendLine("Token Definitions")
        tokens.forEach { token ->
            appendLine(" - $token")
        }
    }
}
