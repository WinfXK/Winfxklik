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
* Created Date: 2024/11/20  08:55 */
package cn.winfxk.libk.config

import cn.winfxk.libk.config.passwd.*
import cn.winfxk.libk.log.Log
import cn.winfxk.libk.tool.utils.*
import com.alibaba.fastjson2.JSONWriter
import java.io.File
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

/**
 * 配置文件工具类
 * @param file 配置文件
 * @param type 配置文件保存类型
 * @param defaultConfig 如果配置文件不存在，导入的默认配置
 * @param passwd 文件密码
 * @param tag 日志标签
 */
class Config(val file: File, val type: Type = Type.Json, val defaultConfig: MutableMap<String, out Any?>? = null, val passwd: Int, val tag: String = Config.tag) {
    private val log = Log(tag);
    private val data = ConcurrentHashMap<String, Any?>();
    private val ready = when (type) {
        Type.Json -> ByJson(file, type, passwd)
        Type.Text -> ByText(file, type, passwd)
        Type.Int  -> ByInt(file, type, passwd)
        Type.Hax  -> ByHax(file, type, passwd)
        Type.Yaml -> ByYaml(file, type, passwd)
    };
    /**
     * 配置文件工具类
     * @param file 配置文件
     */
    constructor(file: File) : this(file, Type.Json, null, 10086);
    /**
     * 配置文件工具类
     * @param file 配置文件
     * @param passwd 文件密码
     */
    constructor(file: File, passwd: Int) : this(file, Type.Text, passwd);
    constructor(file: File, type: Type, passwd: Int) : this(file, type, null, passwd);
    companion object {
        private const val tag = "Config";
    }

    init {
        try {
            if (file.exists() && file.isFile) load();
        } catch (e: Exception) {
            log.e("加载配置文件数据时出现异常！type: ${type.name}", e)
        }
        if (! defaultConfig.isNullOrEmpty()) if (! file.exists() || ! file.isFile) {
            log.i("配置文件不存在，将导入默认配置！")
            data.putAll(defaultConfig);
        }
    }
    /**
     * 清空配置文件数据
     * @return 配置文件
     */
    fun clear() = this.also { it.data.clear() }
    /**
     * 批量填充数据
     * @param map 需要填充的数据
     * @return 配置文件
     */
    fun setAll(map: Map<String, Any?>) = this.also { it.data.putAll(map); }
    /**
     * 返回全部数据
     */
    fun getAll() = HashMap(data);
    /**
     * 返回全部Value
     */
    fun getValues() = ArrayList(data.values);
    /**
     * 返回全部Key
     */
    fun getKeys() = HashSet(data.keys);
    /**
     * 根据值返回对应的Key
     * @param value 可能的值
     * @return Keys
     */
    fun getKeys(value: Any?): HashSet<String> {
        val list = HashSet<String>();
        for ((k, v) in HashMap(data))
            if ((value == null && v == null) || value?.equals(v) == true) list.add(k);
        return list;
    }
    /**
     * 判断是否包含某个值
     */
    fun containsValue(value: Any?): Boolean = data.containsValue(value);
    /**
     * 判断是否包含某个Key
     */
    fun containsKey(key: String): Boolean = data.containsKey(key);
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun get(key: String, default: Any? = null): Any? {
        if (data.containsKey(key)) return data[key];
        return default;
    }
    /**
     * 根据Key读取String数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getString(key: String, default: String? = null): String? = get(key, default).objToString(default)
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getInt(key: String, default: Int = 0): Int = get(key, default).objToInt(default);
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getFloat(key: String, default: Float = 0f): Float = get(key, default).objToFloat(default);
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getDouble(key: String, default: Double = 0.0): Double = get(key, default).objToDouble(default);
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getLong(key: String, default: Long = 0L): Long = get(key, default).objToLong(default);
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getBigDecimal(key: String, default: BigDecimal = BigDecimal.ZERO): BigDecimal = get(key, default).toBigDecimal(default);
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getBoolean(key: String, default: Boolean = false): Boolean = get(key, default).objToBoolean(default);
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getMap(key: String, default: Map<String, Any?>? = HashMap()): Map<String, Any?>? {
        val any = get(key, default);
        if (any == null || any !is Map<*, *>) return default;
        return any as Map<String, Any?>;
    }
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * <br><b>[请注意！此方法返回的一定是新对象！]</b>
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getStringMap(key: String, default: Map<String, String?>? = HashMap()): Map<String, String?>? {
        val any = getMap(key, default) ?: return default;
        val map = HashMap<String, String?>();
        for ((k, v) in any) map[k] = v.objToString(null);
        return map;
    }
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getList(key: String, default: List<*>? = ArrayList<Any?>()): List<Any?>? {
        val any = get(key, default);
        if (any == null || any !is List<*>) return default;
        return any;
    }
    /**
     * 根据Key读取数据，如果Key不存在，则返回一个默认值
     * <br><b>[请注意！此方法返回的一定是新对象！]</b>
     * @param key Key
     * @param default 默认值
     * @return 数据
     */
    fun getStringList(key: String, default: List<String?>? = ArrayList()): List<String?>? {
        val any = getList(key, default) ?: return default;
        val list = ArrayList<String?>();
        for (item in any) list.add(item.objToString(null));
        return list;
    }
    /**
     * 根据Key写入数据
     * @param key Key
     * @param value 数据
     * @return 配置文件
     */
    fun set(key: String, value: Any?) = this.also { it.data[key] = value }
    /**
     * 根据Key删除某个值
     */
    fun remove(key: String): Boolean {
        if (! containsKey(key)) return false;
        data.remove(key);
        return true;
    }
    /**
     * 根据Value删除值
     * @param value Value
     * @return 删除了几个数据
     */
    fun removeByValue(value: Any?): Int {
        if (! containsValue(value)) return 0;
        var count = 0;
        for ((k, v) in HashMap(data))
            if ((v == null && value == null) || value?.equals(v) == true) {
                data.remove(k);
                count ++;
            }
        return count;
    }
    /**
     * 保存配置文件
     * @return 保存结果
     */
    @Synchronized
    fun save(): Boolean {
        try {
            file.parentFile?.also { if (! it.exists() || ! it.isDirectory) if (! it.mkdirs()) log.w("创建配置文件父文件夹${it}时可能已失败！") }
            if (! file.exists() || ! file.isFile) if (! file.createNewFile()) log.w("创建配置文件${file}时可能已经失败！")
            file.write(ready.getString(data));
            return true;
        } catch (e: Exception) {
            log.e("保存配置文件时出现异常！", e)
            return false;
        }
    }
    /**
     * 重载配置文件
     * @return 重载结果
     */
    @Synchronized
    fun reload(): Boolean {
        if (! file.exists() || ! file.isFile) return false;
        return try {
            load();
            true;
        } catch (e: Exception) {
            log.e("重载配置文件数据时出现异常！type: ${type.name}", e)
            false
        }
    }
    /**
     * 加载配置文件
     */
    private fun load() {
        data.clear();
        data.putAll(ready.read())
    }

    override fun toString(): String {
        return """
            Config: {
                Tag: $tag
                File: ${file.absoluteFile}
                Type: $type
                Passwd: $passwd
                Ready: $ready
                Data: ${data.toJsonString()}
            }
        """.trimIndent()
    }
}
