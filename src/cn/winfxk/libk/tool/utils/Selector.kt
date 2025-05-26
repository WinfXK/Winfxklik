package cn.winfxk.libk.tool.utils

import java.util.*

class Selector<T>(private val weights: Map<T, Int>) {
    constructor(vararg selectorables: Selectorable<T>) : this(
        selectorables.associate { it.item to it.getWeight() }
    )

    constructor(vararg entrys: Pair<T, Int>) : this(entrys.associate { it.first to it.second })
    constructor(list: Collection<Selectorable<T>>) : this(
        list.associate { it.item to it.getWeight() }
    )

    private val random = Random()
    private val cumulativeWeights: List<Pair<T, Int>>
    private val totalWeight: Int
    fun getTotalWeight() = totalWeight;
    fun getMap() = weights;
    fun items() = ArrayList(weights.keys);

    init {
        var cumulativeWeight = 0
        cumulativeWeights = weights.map { (key, value) ->
            require(value > 0) { "Weight must be positive: $key=$value" }
            cumulativeWeight += value
            key to cumulativeWeight
        }
        totalWeight = cumulativeWeight
    }

    fun select(): T {
        val target = random.nextInt(totalWeight)
        for ((item, cumulative) in cumulativeWeights)
            if (target < cumulative) return item
        throw IllegalStateException("Selection failed. This should not happen.")
    }
}

interface Selectorable<T> {
    val item: T
    fun getWeight(): Int
}
