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
* Created Date: 2024/11/20  09:24 */
package cn.winfxk.libk.config.passwd

import cn.winfxk.libk.config.Type
import cn.winfxk.libk.tool.utils.toJson
import cn.winfxk.libk.tool.utils.toJsonString
import java.io.File

open class ByJson(file: File, type: Type, passwd: Int) : BaseReady(file, type, passwd) {
    override fun read(): MutableMap<String, Any?> = getContext().toJson();
    override fun getString(map: MutableMap<String, Any?>): String = map.toJsonString();
}