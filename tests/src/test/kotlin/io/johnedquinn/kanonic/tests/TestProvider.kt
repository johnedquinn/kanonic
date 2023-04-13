package io.johnedquinn.kanonic.tests

import java.util.stream.Stream
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

open class TestProvider<T>(private val args: List<T>) : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return args.stream().map { Arguments.of(it) }
    }
}
