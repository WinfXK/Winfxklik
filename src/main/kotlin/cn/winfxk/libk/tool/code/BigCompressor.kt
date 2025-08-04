/* 
* Copyright Notice
* © [2024] Winfxk. All rights reserved.
* The software, its source code, and all related documentation are the intellectual property of Winfxk. Any reproduction or distribution of this software or any part thereof must be clearly attributed to Winfxk and the original author. Unauthorized copying, reproduction, or distribution without proper attribution is strictly prohibited.
* For inquiries, support, or to request permission for use, please contact us at:
* Email: admin@winfxk.cn
* QQ: 2508543202
* Visit our homepage for more information: http://Winfxk.cn
* 
* --------- Create message ---------
* Created by IntelliJ ID
* Author： Winfxk
* Created PCUser: kc4064 
* Web: http://winfxk.com
* Created Date: 2025/5/26  08:13 */
package cn.winfxk.libk.tool.code

import java.math.BigInteger


object BigCompressor {
    private val BASE94_CHARS: CharArray = CharArray(94)
    private  val BASE: Int = BASE94_CHARS.size

    init {
        for (i in 0 ..< BASE94_CHARS.size)
            BASE94_CHARS[i] = (33 + i).toChar();
    }

    fun compress(number: Int) = compress(number.toLong());
    fun compress(number: Long) = compress(BigInteger.valueOf(number));
    fun compress(number: BigInteger): String {
        var number = number
        if (number.compareTo(BigInteger.ZERO) == 0) return BASE94_CHARS[0].toString()
        val sb = StringBuilder()
        var divMod: Array<BigInteger>
        while (number > BigInteger.ZERO) {
            divMod = number.divideAndRemainder(BigInteger.valueOf(BASE.toLong()))
            sb.append(BASE94_CHARS[divMod[1].toInt()])
            number = divMod[0]
        }
        return sb.reverse().toString()
    }

    fun decompress(input: String): BigInteger {
        var result = BigInteger.ZERO
        for (i in 0 ..< input.length) {
            val value = input.get(i).code - 33
            result = result.multiply(BigInteger.valueOf(BASE.toLong())).add(BigInteger.valueOf(value.toLong()))
        }
        return result
    }
}
