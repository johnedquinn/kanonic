@file:JvmName("Main")

package io.johnedquinn.kanonic.syntax

import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val command = CommandLine(GenerateSyntax())
    val exitCode = command.execute(*args)
    exitProcess(exitCode)
}
