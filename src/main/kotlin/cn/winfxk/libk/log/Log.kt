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
* Created Date: 2024/11/19  16:11 */
package cn.winfxk.libk.log

import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

class Log(val tag: String) {
    private val listeners = ArrayList<OnLogListener>();
    /**
     * 打印一条日志
     */
    @JvmOverloads
    fun i(message: Any? = null, throwable: Throwable? = null) = onLog(Type.Info, tag, message, throwable);
    /**
     * 打印一条Debug日志
     */
    @JvmOverloads
    fun d(message: Any? = null, throwable: Throwable? = null) = onLog(Type.Debug, tag, message, throwable);
    /**
     * 打印一条警告日志
     */
    @JvmOverloads
    fun w(message: Any? = null, throwable: Throwable? = null) = onLog(Type.Warning, tag, message, throwable);
    /**
     * 打印一条异常日志
     */
    @JvmOverloads
    fun e(message: Any? = null, throwable: Throwable? = null) = onLog(Type.Error, tag, message, throwable);
    private fun onLog(type: Type, tag: String, message: Any?, throwable: Throwable?) = onLog(type, tag, message, throwable, listeners)
    /**
     * 新增日志事件监听器
     */
    fun addListener(listener: OnLogListener) {
        if (! Companion.listeners.contains(listener)) Companion.listeners.add(listener);
    }
    /**
     * 获取所有的日志管理器清单
     */
    fun getListeners() = Companion.listeners;
    /**
     * 删除日志事件管理器
     */
    fun removeListener(listener: OnLogListener): Boolean {
        if (! Companion.listeners.contains(listener)) return false;
        Companion.listeners.remove(listener);
        return true;
    }
    /**
     * 清除字符串内的颜色代码
     */
    fun clearColor(content: String?): String? {
        var string = content;
        if (string.isNullOrBlank()) return string;
        for (color in colors) string = string?.replace(color, "");
        return string;
    }

    companion object {
        private val listeners = ArrayList<OnLogListener>();
        private val emptyList = ArrayList<OnLogListener>();
        private const val defaultColor = "\u001b[0m";
        private val colors = arrayOf(defaultColor, Type.Info.color, Type.Debug.color, Type.Error.color, Type.Warning.color)
        private val title = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        /**
         * 打印一条日志
         */
        @JvmOverloads
        fun i(tag: String, message: Any? = null, throwable: Throwable? = null) {
            if (message == null && throwable == null)
                onLog(Type.Info, null, tag, null);
            else onLog(Type.Info, tag, message, throwable);
        }
        /**
         * 打印一条Debug日志
         */
        @JvmOverloads
        fun d(tag: String, message: Any? = null, throwable: Throwable? = null) {
            if (message == null && throwable == null)
                onLog(Type.Debug, null, tag, null);
            else onLog(Type.Debug, tag, message, throwable);
        }
        /**
         * 打印一条警告日志
         */
        @JvmOverloads
        fun w(tag: String, message: Any? = null, throwable: Throwable? = null) {
            if (message == null && throwable == null)
                onLog(Type.Warning, null, tag, null);
            else onLog(Type.Warning, tag, message, throwable);
        }
        /**
         * 打印一条异常日志
         */
        @JvmOverloads
        fun e(tag: String, message: Any? = null, throwable: Throwable? = null) {
            if (message == null && throwable == null)
                onLog(Type.Error, null, tag, null);
            else onLog(Type.Error, tag, message, throwable);
        }

        private fun onLog(type: Type, tag: String?, message: Any?, throwable: Throwable?) = onLog(type, tag, message, throwable, emptyList)

        private fun onLog(type: Type, tag: String?, message: Any?, throwable: Throwable?, listeners: ArrayList<OnLogListener>) {
            val error = throwable?.let {
                val stringWriter = StringWriter();
                it.printStackTrace(PrintWriter(stringWriter));
                stringWriter.toString()
            } ?: ""
            val msg = message?.toString() ?: "";
            val title = getTitle();
            val log = "$defaultColor$title${if (tag.isNullOrBlank()) "" else "[$tag]"}${type.title}: ${type.color}${msg.let { if (it.isBlank()) "" else "$it${if (error.isBlank()) "" else "\n"}" }}$error$defaultColor";
            println(log)
            listeners.forEach { it.logListener(type, tag, message, throwable, title, log) }
            Log.listeners.forEach { it.logListener(type, tag, message, throwable, title, log) }
        }
        /**
         * 获取日志打印的时间头
         */
        fun getTitle(): String = "[${title.format(Date())}]"
        /**
         * 新增日志事件监听器
         */
        fun addListener(listener: OnLogListener) {
            if (! listeners.contains(listener)) listeners.add(listener);
        }
        /**
         * 获取所有的日志管理器清单
         */
        fun getListeners() = listeners;
        /**
         * 删除日志事件管理器
         */
        fun removeListener(listener: OnLogListener): Boolean {
            if (! listeners.contains(listener)) return false;
            listeners.remove(listener);
            return true;
        }
        /**
         * 清除字符串内的颜色代码
         */
        fun clearColor(content: String?): String? {
            var string = content;
            if (string.isNullOrBlank()) return string;
            for (color in colors) string = string?.replace(color, "");
            return string;
        }
    }
}