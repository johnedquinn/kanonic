package io.johnedquinn.calculator.interpreter

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

open class TestProvider<T>(private val args: List<T>) : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return args.stream().map { Arguments.of(it) }
    }
}
