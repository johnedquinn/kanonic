package io.johnedquinn.kanonic.tests.kanonic

import java.io.File
import java.nio.charset.Charset

object GrammarProvider {
    public fun provide(path: String): String {
        val url = this.javaClass.classLoader.getResource(path)?.toURI() ?: error("RESOURCE ($path) NOT FOUND")
        val file = File(url).inputStream()
        return file.readBytes().toString(Charset.defaultCharset())
    }
}
