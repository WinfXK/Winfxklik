package cn.winfxk.libk.tool.utils

import java.math.BigDecimal


/**
 * 强制转换为int
 */
fun Any?.objToInt(default: Int = 0): Int {
    if (this == null) return default;
    return try {
        if (this is Number) return this.toInt();
        this.toBigDecimal(BigDecimal(default)).toInt();
    } catch (_: Exception) {
        default;
    }
}
/**
 * 将一个字符串强制转换为Long
 */
fun Any?.objToLong(default: Long = 0): Long {
    if (this == null) return default;
    return try {
        if (this is Number) return this.toLong();
        this.toBigDecimal(BigDecimal(default)).toLong();
    } catch (_: Exception) {
        default;
    }
}
/**
 * 强制转换为BigDecimal
 */
fun Any?.toBigDecimal(default: BigDecimal = BigDecimal.ZERO): BigDecimal {
    if (this == null) return default;
    return try {
        if (this is Number) return BigDecimal(this.toString());
        BigDecimal(this.toString());
    } catch (_: Exception) {
        default;
    }
}

fun Any?.objToFloat(default: Float = 0.0f): Float {
    if (this == null) return default;
    return try {
        if (this is Number) return this.toFloat();
        this.toBigDecimal(BigDecimal(default.toString())).toFloat();
    } catch (_: Exception) {
        default;
    }
}

fun Any?.objToDouble(default: Double = 0.0): Double {
    if (this == null) return default;
    return try {
        if (this is Number) return this.toDouble();
        this.toBigDecimal(BigDecimal(default.toString())).toDouble();
    } catch (_: Exception) {
        default;
    }
}