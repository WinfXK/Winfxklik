/*Created by IntelliJ IDEA.
  User: kc4064 
  Date: 2024/6/22  上午8:26*/
package cn.winfxk.libk.tool.utils.field;


import cn.winfxk.libk.log.Log;
import cn.winfxk.libk.tool.tab.Tabable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Fields extends Tabable {
    default Map<String, Object> getAllFields() {
        return getAllFields(new ArrayList<>());
    }

    default Map<String, Object> getAllFields(List<Integer> hash) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
            try {
                if (field.isAnnotationPresent(Ignore.class))
                    continue;
                if (hash.contains(field.hashCode())) continue;
                hash.add(field.hashCode());
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(this);
                if (fieldValue instanceof Fields) fieldValue = ((Fields) fieldValue).getAllFields(hash);
                map.put(fieldName, fieldValue);
            } catch (IllegalAccessException e) {
                Log.Companion.w(getTAG(), "获取" + getClass().getSimpleName() + "属性数据时出现异常！", e);
            }
        return map;
    }
}
