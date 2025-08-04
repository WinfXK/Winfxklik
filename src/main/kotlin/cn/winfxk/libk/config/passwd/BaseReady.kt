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
* Created Date: 2024/11/20  09:23 */
package cn.winfxk.libk.config.passwd

import cn.winfxk.libk.config.Type
import cn.winfxk.libk.tool.utils.readString
import java.io.File

abstract class BaseReady(val file: File, val type: Type, val passwd: Int) {
    /**
     * 获取的文本内容
     */
    open fun getContext(): String? = if (! file.exists() || ! file.isFile) null else file.readString()
    /**
     * 获取的序列化内容
     */
    abstract fun read(): MutableMap<String, Any?>;
    abstract fun getString(map: MutableMap<String, Any?>): String;
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}