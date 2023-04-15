package io.johnedquinn.kanonic.runtime.error

open class KanonicException(
    override val message: String?,
    override val cause: Throwable?
) : RuntimeException(message, cause)
