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
* Created Date: 2025/5/26  08:00 */
package cn.winfxk.libk.tool.utils

/**
 * 雪花ID生成器
 *@param datacenterId 数据中心ID
 *@param machineId 机器ID
 */
class SnowflakeID(
    private val datacenterId: Long,
    private val machineId: Long
) {
    private var lastTimestamp = - 1L
    private var sequence = 0L

    init {
        require(datacenterId in 0 .. maxDatacenterId) { "数据中心ID不能超过 $maxDatacenterId ！" }
        require(machineId in 0 .. maxMachineId) { "机器ID不能超过 $maxMachineId ！" }
    }
    /**
     * 返回一个Key
     */
    @Synchronized
    fun nextId(): Long {
        var timestamp = currentTime()
        if (timestamp < lastTimestamp)
            throw RuntimeException("系统时间回退了！小姬子晕了啦！时间穿越不被允许！嘤嘤嘤！")
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) and sequenceMask
            if (sequence == 0L) timestamp = waitUntilNextMillis(lastTimestamp)
        } else sequence = 0L
        lastTimestamp = timestamp
        return ((timestamp - twepoch) shl timestampLeftShift.toInt()) or
                (datacenterId shl datacenterIdShift.toInt()) or
                (machineId shl machineIdShift.toInt()) or sequence
    }

    private fun waitUntilNextMillis(lastTimestamp: Long): Long {
        var timestamp = currentTime()
        while (timestamp <= lastTimestamp) timestamp = currentTime()
        return timestamp
    }

    private fun currentTime(): Long = System.currentTimeMillis()

    companion object {
        private const val twepoch = 1672531200000L
        private const val datacenterIdBits = 5L
        private const val machineIdBits = 5L
        private const val sequenceBits = 12L
        private const val maxDatacenterId = - 1L xor (- 1L shl datacenterIdBits.toInt())
        private const val maxMachineId = - 1L xor (- 1L shl machineIdBits.toInt())
        private const val machineIdShift = sequenceBits
        private const val datacenterIdShift = sequenceBits + machineIdBits
        private const val timestampLeftShift = sequenceBits + machineIdBits + datacenterIdBits
        private const val sequenceMask = - 1L xor (- 1L shl sequenceBits.toInt())
    }
}