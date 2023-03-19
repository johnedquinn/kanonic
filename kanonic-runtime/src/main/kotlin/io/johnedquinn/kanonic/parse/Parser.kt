package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.machine.ParseTable

public interface Parser {
    public val info: ParserMetadata
    public val grammar: Grammar
    public val table: ParseTable
    public fun parse(input: String): Node
}
