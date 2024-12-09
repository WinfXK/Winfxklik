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
import cn.winfxk.libk.tool.utils.objToInt
import java.io.File

open class ByInt(file: File, type: Type, passwd: Int) : ByJson(file, type, passwd) {
    override fun getContext(): String? = super.getContext().let { if (it.isNullOrBlank()) it else getContext(it) }
    /**
     * 返回解密后的文本
     */
    open fun getContext(string: String): String {
        val buffer = StringBuilder();
        val array = string.split(getSpils());
        for (s in array) if (s.isNotBlank()) buffer.append(getString(s.objToInt(), passwd))
        return buffer.toString();
    }

    override fun getString(map: MutableMap<String, Any?>): String = passwdString(super.getString(map))
    /**
     * 返回加密后的文本
     */
    open fun passwdString(string: String): String {
        val buffer = StringBuilder();
        for (char in string.toCharArray()) {
            buffer.append(getPasswd(char, passwd)).append(getSpils())
        }
        return buffer.toString();
    }
    /**
     * 加密分隔符
     */
    open fun getSpils(): String = spils;

    companion object {
        private const val spils = ".";
        fun getString(code: Int, passwd: Int): String {
            var fullcode = code - passwd;
            if (fullcode < 0) fullcode = fullcode + 65535
            return fullcode.toChar().toString();
        }

        fun getPasswd(char: Char, passwd: Int): Int {
            var code = char.code + passwd;
            if (code >= 65535) code = code - 65535;
            return code;
        }
    }
}