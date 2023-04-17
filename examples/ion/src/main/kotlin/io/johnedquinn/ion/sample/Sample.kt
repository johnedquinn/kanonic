package io.johnedquinn.ion.sample

interface Inter<T> {
    public fun getObject(id: T): Double
    public fun getObjectWithDefault(id: T): Double {
        return 2.0
    }
}

class InterA : Inter<Int> {
    override fun getObject(id: Int): Double {
        return 1.0
    }
}

class InterB : Inter<String> {
    override fun getObject(id: String): Double {
        return 1.0
    }

    override fun getObjectWithDefault(id: String): Double {
        return 3.0
    }
}
