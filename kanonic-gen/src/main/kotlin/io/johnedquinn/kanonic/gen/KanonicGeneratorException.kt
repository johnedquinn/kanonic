package io.johnedquinn.kanonic.gen

class KanonicGeneratorException(
    override val message: String?,
    override val cause: Throwable?
) : Exception()
