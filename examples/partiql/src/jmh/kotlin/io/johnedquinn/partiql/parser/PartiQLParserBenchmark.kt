package io.johnedquinn.partiql.parser

import com.amazon.ionelement.api.createIonElementLoader
import io.johnedquinn.kanonic.runtime.parse.KanonicParser
import io.johnedquinn.partiql.generated.PartiQLSpecification
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
import org.partiql.lang.syntax.PartiQLParserBuilder
import java.util.concurrent.TimeUnit


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Suppress("UNUSED")
internal open class PartiQLParserBenchmark {
    companion object {
        private const val FORK_VALUE: Int = 1 // 2
        private const val MEASUREMENT_ITERATION_VALUE: Int = 5 // 10
        private const val MEASUREMENT_TIME_VALUE: Int = 1
        private const val WARMUP_ITERATION_VALUE: Int = 3 // 5
        private const val WARMUP_TIME_VALUE: Int = 1
    }

    @State(Scope.Thread)
    open class MyState {
        val parser = PartiQLParser
        val kanonicParser = KanonicParser.Builder.standard().withSpecification(PartiQLSpecification).build()
        val partiQLParser = PartiQLParserBuilder.standard().build()

        @Param(
            "SELECT a FROM b",
            "SELECT (SELECT a FROM b) FROM b",
            "SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)"
        )
        public var inputIndex: String = ""

        var inputs = arrayOf<String>(
            "SELECT a FROM b",
            "SELECT (SELECT a FROM b) FROM b",
            "SELECT (SELECT a FROM (SELECT a FROM b)) FROM (SELECT (SELECT a FROM b) FROM c)"
        )
    }

//    @Benchmark
//    @Fork(value = FORK_VALUE)
//    @Measurement(iterations = MEASUREMENT_ITERATION_VALUE, time = MEASUREMENT_TIME_VALUE)
//    @Warmup(iterations = WARMUP_ITERATION_VALUE, time = WARMUP_TIME_VALUE)
//    @Suppress("UNUSED")
//    fun parseUsingKanonic(state: MyState, blackhole: Blackhole) {
//        val expr = state.kanonicParser.parse(state.inputIndex)
//        blackhole.consume(expr)
//    }

    @Benchmark
    @Fork(value = FORK_VALUE)
    @Measurement(iterations = MEASUREMENT_ITERATION_VALUE, time = MEASUREMENT_TIME_VALUE)
    @Warmup(iterations = WARMUP_ITERATION_VALUE, time = WARMUP_TIME_VALUE)
    @Suppress("UNUSED")
    fun parseUsingNewPartiQLParser(state: MyState, blackhole: Blackhole) {
        val expr = state.parser.parseAstStatement(state.inputIndex)
        blackhole.consume(expr)
    }

    @Benchmark
    @Fork(value = FORK_VALUE)
    @Measurement(iterations = MEASUREMENT_ITERATION_VALUE, time = MEASUREMENT_TIME_VALUE)
    @Warmup(iterations = WARMUP_ITERATION_VALUE, time = WARMUP_TIME_VALUE)
    @Suppress("UNUSED")
    fun parseUsingOldPartiQLParser(state: MyState, blackhole: Blackhole) {
        val expr = state.partiQLParser.parseAstStatement(state.inputIndex)
        blackhole.consume(expr)
    }
}
