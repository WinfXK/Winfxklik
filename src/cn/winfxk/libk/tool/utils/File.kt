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
* Created Date: 2024/11/19  16:10 */
package cn.winfxk.libk.tool.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 * 从一个输入流写入数据到文件
 */
fun File.write(input: InputStream, cacheSize: Int = 1024) {
    input.use { i ->
        FileOutputStream(this).use { out ->
            var length: Int;
            val buffer = ByteArray(cacheSize);
            while (i.read(buffer).also { length = it } != - 1)
                out.write(buffer, 0, length)
        }
    }
}
/**
 * 删除一个文件夹或者文件
 * <b>此操作将地柜删除</b>
 */
fun File.deletes(): Boolean {
    if (this.isFile) return this.delete();
    var isSuccess = true;
    this.listFiles()?.forEach { isSuccess = it.deletes(); }
    return this.delete() && isSuccess;
}
/**
 * 将字符串保存到文件
 * @param string 需要保存的字符串
 * @param charset 字符串编码
 */
fun File.write(string: String, charset: Charset = Charsets.UTF_8) = write(string.byteInputStream(charset))
/**
 * 从文件中读取文本
 * @param charset 字符编码
 * @param 缓冲区大小
 */
fun File.readString(charset: Charset = Charsets.UTF_8): String = FileInputStream(this).readString(charset);