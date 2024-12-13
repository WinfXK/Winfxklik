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
* Created Date: 2024/11/19  17:21 */
package cn.winfxk.libk.tool.utils

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter

private val emptyJsonObject = JSONObject();
private val emptyJsonArray = JSONArray();
/**
 * 将字符串序列化为JsonObject
 */
fun String?.toJson(): JSONObject {
    if (this == null) return JSONObject();
    return JSONObject.parseObject(this);
}
/**
 * 将字符串反序列化为JsonArray
 */
fun String?.toArray(): JSONArray {
    if (this == null) return JSONArray();
    return JSONArray.parseArray(this);
}
/**
 * 将Map序列化为JsonString
 */
@JvmOverloads fun MutableMap<String, Any?>?.toJsonString(pretty: JSONWriter.Feature = JSONWriter.Feature.PrettyFormat): String = if (this == null) emptyJsonObject.toJSONString(pretty) else JSONObject.toJSONString(this, pretty);

/**
 * 将List序列化为JsonString
 */
@JvmOverloads fun List<Any?>?.toJsonString(pretty: JSONWriter.Feature = JSONWriter.Feature.PrettyFormat): String = if (this == null) emptyJsonArray.toJSONString(pretty) else JSONArray.toJSONString(this,pretty);