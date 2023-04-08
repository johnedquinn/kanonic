package io.johnedquinn.kanonic.tool

import io.johnedquinn.kanonic.gen.KanonicGenerator
import io.johnedquinn.kanonic.runtime.grammar.GrammarBuilder
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.runtime.utils.Logger
import io.johnedquinn.kanonic.syntax.generated.KanonicSpecification
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

    @CommandLine.Option(names = ["-d", "--debug"], description = ["Prints debug statements"])
    var debug: Boolean = false

    @CommandLine.Option(names = ["-o", "--output"], description = ["Output directory to place generated packages."], paramLabel = "<directory>")
    var output: File? = null

    @CommandLine.Parameters(arity = "1", index = "0", description = ["The Kanonic grammar file."], paramLabel = "KANONIC_FILE")
    var file: File? = null

    override fun run() {
        if (debug) { Logger.tolerance = Logger.Tolerance.DEBUG }
        val fileContent = file?.reader()?.readText() ?: error("Unable to read file content. Exiting.")

        val parser = KanonicParser.Builder
            .standard()
            .withSpecification(KanonicSpecification)
            .build()
        val ast = parser.parse(fileContent)

        Logger.debug("Parsed Kanonic File into AST:")
        Logger.debug(KanonicNodeFormatter.format(ast))

        // Convert the Kanonic AST --> Grammar
        val grammar = AstConverter.convert(ast)

        // Generate Files
        val files = KanonicGenerator.generate(grammar)
        when (output) {
            null -> files.forEach { it.writeTo(System.out) }
            else -> files.forEach { it.writeTo(output!!) }
        }
    }
}
