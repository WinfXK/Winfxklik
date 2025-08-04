package cn.winfxk.libk.tool.utils

import java.math.BigDecimal


/**
 * 将一个未知值转换为Int
 * @param default 若转换失败返回的默认值
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
 * 将一个未知值转换为Long
 * @param default 若转换失败返回的默认值
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
 * 将一个未知值转换为BigDecimal
 * @param default 若转换失败返回的默认值
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
/**
 * 将一个未知值转换为Float
 * @param default 若转换失败返回的默认值
 */
fun Any?.objToFloat(default: Float = 0.0f): Float {
    if (this == null) return default;
    return try {
        if (this is Number) return this.toFloat();
        this.toBigDecimal(BigDecimal(default.toString())).toFloat();
    } catch (_: Exception) {
        default;
    }
}
/**
 * 将一个未知值转换为Double
 * @param default 若转换失败返回的默认值
 */
fun Any?.objToDouble(default: Double = 0.0): Double {
    if (this == null) return default;
    return try {
        if (this is Number) return this.toDouble();
        this.toBigDecimal(BigDecimal(default.toString())).toDouble();
    } catch (_: Exception) {
        default;
    }
}
/**
 * 将一个未知值转换为Boolean
 * @param default 若转换失败返回的默认值
 */
fun Any?.objToBoolean(default: Boolean = false): Boolean {
    if (this == null) return default;
    return try {
        when (this) {
            is Boolean -> this
            is Number  -> when (this.toInt()) {
                1    -> true
                0    -> false
                else -> default
            }
            is String  -> when (this.lowercase()) {
                "1"    -> true
                "true" -> true
                else   -> default
            }
            else       -> default
        }
    } catch (_: Exception) {
        default;
    }
}
/**
 * 将一个未知内容转换为字符串，
 * @param default 如果字符串为null，返回默认值
 */
fun Any?.objToString(default: String? = null): String? {
    if (this == null) return default;
    return this.toString();
}
/**
 * 将传入的RGB值转换为16进制颜色符
 */
fun rgbToHex(r: Int, g: Int, b: Int): Int {
    require(r in 0 .. 255 && g in 0 .. 255 && b in 0 .. 255) { "RGB values must be in the range 0 to 255" }
    return (r shl 16) or (g shl 8) or b
}
/**
 * 将16进制颜色符转换为RGB
 */
fun hexToRGB(hex: Int) = Triple((hex shr 16) and 0xFF, (hex shr 8) and 0xFF, hex and 0xFF);