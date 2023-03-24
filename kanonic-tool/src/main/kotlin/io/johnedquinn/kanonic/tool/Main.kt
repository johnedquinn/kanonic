@file:JvmName("Main")

package io.johnedquinn.kanonic.tool

import io.johnedquinn.kanonic.tool.pico.GenerateCommand
import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val command = CommandLine(GenerateCommand())
    val exitCode = command.execute(*args)
    exitProcess(exitCode)
}
