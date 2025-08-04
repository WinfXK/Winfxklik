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
import cn.winfxk.libk.tool.Tool
import java.io.File

class ByHax(file: File, type: Type, passwd: Int) : ByInt(file, type, passwd) {
    override fun passwdString(string: String): String {
        val buffer = StringBuilder();
        for (char in string.toCharArray())
            buffer.append(Tool.CompressNumber(getPasswd(char, passwd).toLong())).append(getSpils())
        return buffer.toString();
    }

    override fun getContext(string: String): String {
        val buffer = StringBuilder();
        val array = string.split(getSpils());
        for (s in array) if (s.isNotBlank()) buffer.append(getString(Tool.UnCompressNumber(s).toInt(), passwd))
        return buffer.toString();
    }

    override fun getSpils(): String {
        return spils
    }

    companion object {
        private const val spils = ".";
    }
}
