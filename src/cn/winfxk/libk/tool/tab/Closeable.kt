/*Created by IntelliJ IDEA.
  User: kc4064 
  Date: 2024/7/1  上午9:26*/
package cn.winfxk.libk.tool.tab

import cn.winfxk.libk.log.Log
import java.lang.AutoCloseable
import java.lang.Exception


interface Closeable {
    fun close(vararg closes: AutoCloseable?) {
        if (closes == null) return
        for (close in closes) if (close != null) try {
            close.close()
        } catch (e: Exception) {
            Log.w("Closeable", "关闭流" + close.javaClass.getSimpleName() + "时出现异常！", e)
        }
    }
}