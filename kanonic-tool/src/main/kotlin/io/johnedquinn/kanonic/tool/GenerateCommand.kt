package io.johnedquinn.kanonic.tool

import io.johnedquinn.kanonic.gen.KanonicGenerator
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.kanonic.runtime.utils.KanonicLogger
import io.johnedquinn.kanonic.syntax.generated.KanonicSpecification
import picocli.CommandLine
import java.io.File
import java.util.logging.ConsoleHandler
import kotlin.system.exitProcess

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

    private val consoleHandler = ConsoleHandler()
    private val logger = KanonicLogger.getLogger()
    init {
        logger.addHandler(consoleHandler)
    }

    @CommandLine.Option(names = ["--log-level"], description = ["Prints debug statements"])
    var logLevel: KanonicLogger.LogLevel = KanonicLogger.LogLevel.SEVERE

    @CommandLine.Option(names = ["-o", "--output"], description = ["Output directory to place generated packages."], paramLabel = "<directory>")
    var output: File? = null

    @CommandLine.Parameters(arity = "1", index = "0", description = ["The Kanonic grammar file/directory."], paramLabel = "KANONIC_PATH")
    var file: File? = null

    override fun run() {
        // Set Log Level
        logger.level = logLevel.getJavaLogLevel()
        consoleHandler.level = logLevel.getJavaLogLevel()

        if (file == null) {
            logger.severe("File passed is null!")
            return
        }

        val files: List<File> = when (file!!.isDirectory) {
            false -> listOf(file!!)
            true -> file!!.listFiles()?.toList() ?: emptyList()
        }

        try {
            writeOutput(files, output)
        } catch (t: Throwable) {
            t.printStackTrace()
            exitProcess(1)
        }
    }

    private fun writeOutput(files: List<File>, output: File?) {
        files.forEach { file ->
            generate(file, output)
        }
    }

    private fun generate(file: File, output: File?) {
        val fileContent = file.reader().readText()

        val parser = KanonicParser.Builder
            .standard()
            .withSpecification(KanonicSpecification)
            .build()
        val ast = parser.parse(fileContent)

        logger.fine("Parsed Kanonic File into AST:")
        logger.fine(KanonicNodeFormatter.format(ast))

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
