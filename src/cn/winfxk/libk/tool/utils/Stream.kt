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
* Created Date: 2024/11/20  08:05 */
package cn.winfxk.libk.tool.utils

import cn.winfxk.libk.log.Log
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import javax.swing.JOptionPane

/**
 * 从流加载字体
 */
fun InputStream.readFont(): Font {
    return this.use { Font.createFont(Font.TRUETYPE_FONT, this) };
}
/**
 * 从流中读取文本
 * @param charset 字符编码
 */
fun InputStream.readString(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}
/**
 * 从Jar包中读取资源
 */
fun Any.getStreamByJar(name: String): InputStream? {
    if (name.isBlank()) return null;
    val path = "resources/$name";
    var inputStream: InputStream?;
    inputStream = this.javaClass.getResourceAsStream("/$path");
    if (inputStream == null) {
        Log.w("StreamUtils", "getStreamByJar: 无法从主资源路径获取资源${name}，正在切换..")
        inputStream = this.javaClass.getResourceAsStream("/$name");
    }
    if (inputStream == null) {
        Log.w("StreamUtils", "getStreamByJar: 无法从辅资源路径获取资源${name}，正在切换文件系统.")
        inputStream = Files.newInputStream(Paths.get(path));
    }
    if (inputStream == null) Log.w("StreamUtils", "getStreamByJar: 无法读取资源${name}，请检查是否存在.")
    return inputStream;
}
/**
 *从jar包读取图像资源
 */
fun Any.getImageByjar(name: String): BufferedImage? {
    return this.getStreamByJar(name)?.use { ImageIO.read(it) };
}
/**
 * 抛出一个异常
 */
fun <V> throwException(message: String): V {
    JOptionPane.showMessageDialog(null, message)
    throw IllegalStateException(message);
}