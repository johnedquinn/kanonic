package io.johnedquinn.ion.parser

import com.amazon.ionelement.api.createIonElementLoader
import io.johnedquinn.ion.generated.IonSpecification
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Suppress("UNUSED")
internal open class IonParserBenchmark {
    companion object {
        private const val FORK_VALUE: Int = 1 // 2
        private const val MEASUREMENT_ITERATION_VALUE: Int = 5 // 10
        private const val MEASUREMENT_TIME_VALUE: Int = 1
        private const val WARMUP_ITERATION_VALUE: Int = 3 // 5
        private const val WARMUP_TIME_VALUE: Int = 1
    }

    @State(Scope.Thread)
    open class MyState {
        val parser = IonParser
        val kanonicParser = KanonicParser.Builder.standard().withSpecification(IonSpecification).build()
        val loader = createIonElementLoader()

        @Param(
            "hello",
            "(a::hello b::world my name is John Quinn)",
            "(a b c d e f g h i j k l m n o p q r s t u v w x y z a b c d e f g h i j k l m n o p q r s t u v w x y z)"
        )
        public var inputIndex: String = ""

        var inputs = arrayOf<String>(
            "hello",
            "(a::hello b::world my name is John Quinn)",
            """
                (a b c d e f g h i j k l m n o p q r s t u v w x y z a b c d e f g h i j k l m n o p q r s t u v w x y z)
            """
        )
    }

    @Benchmark
    @Fork(value = FORK_VALUE)
    @Measurement(iterations = MEASUREMENT_ITERATION_VALUE, time = MEASUREMENT_TIME_VALUE)
    @Warmup(iterations = WARMUP_ITERATION_VALUE, time = WARMUP_TIME_VALUE)
    @Suppress("UNUSED")
    fun parseUsingKanonic(state: MyState, blackhole: Blackhole) {
        val expr = state.kanonicParser.parse(state.inputIndex)
        blackhole.consume(expr)
    }

    @Benchmark
    @Fork(value = FORK_VALUE)
    @Measurement(iterations = MEASUREMENT_ITERATION_VALUE, time = MEASUREMENT_TIME_VALUE)
    @Warmup(iterations = WARMUP_ITERATION_VALUE, time = WARMUP_TIME_VALUE)
    @Suppress("UNUSED")
    fun parseUsingIonParser(state: MyState, blackhole: Blackhole) {
        val expr = state.parser.parse(state.inputIndex)
        blackhole.consume(expr)
    }

    @Benchmark
    @Fork(value = FORK_VALUE)
    @Measurement(iterations = MEASUREMENT_ITERATION_VALUE, time = MEASUREMENT_TIME_VALUE)
    @Warmup(iterations = WARMUP_ITERATION_VALUE, time = WARMUP_TIME_VALUE)
    @Suppress("UNUSED")
    fun parseUsingIonLoader(state: MyState, blackhole: Blackhole) {
        val expr = state.loader.loadSingleElement(state.inputIndex)
        blackhole.consume(expr)
    }
}
