package io.johnedquinn.kanonic.runtime.error

open class KanonicRuntimeException(
    override val message: String?,
    override val cause: Throwable?
) : KanonicException(message, cause)