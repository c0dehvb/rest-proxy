package com.midea.cmms.rest.proxy.server.util;

import org.springframework.validation.DataBinder;

import java.lang.reflect.Field;

/**
 * Created by liyilin on 2017/7/18.
 */
public abstract class CopyUtils {

    /**
     * 浅拷贝
     * @param src
     * @param dest
     */
    public static void copy(Object src, Object dest) {
        Field[] fields = src.getClass().getDeclaredFields();
        DataBinder dataBinder = new DataBinder(null);
        for (Field srcField : fields) {
            try {
                Field destField = dest.getClass().getDeclaredField(srcField.getName());
                srcField.setAccessible(true);
                Object value = srcField.get(src);
                destField.setAccessible(true);
                try {
                    Object o = dataBinder.convertIfNecessary(value, destField.getType());
                    destField.set(dest, o);
                } catch (Exception e) {
                    destField.set(dest, value);
                }
            } catch (Exception e) {}
        }
    }

    /**
     * 深拷贝
     * @param src
     * @param dest
     */
    public static void deepCopy(Object src, Object dest) {
        Field[] fields = src.getClass().getDeclaredFields();
        DataBinder dataBinder = new DataBinder(null);
        for (Field srcField : fields) {
            try {
                Field destField = dest.getClass().getDeclaredField(srcField.getName());
                if (srcField.getType().isPrimitive() && destField.getType().isPrimitive()) {
                    Object value = srcField.get(src);
                    Object o = dataBinder.convertIfNecessary(value, destField.getType());
                    destField.setAccessible(true);
                    destField.set(dest, o);
                } else if (srcField.getType() == destField.getType()) {
                    Object srcVal = srcField.get(src);
                    Object destVal = destField.get(dest);
                    deepCopy(srcVal, destVal);
                }
            } catch (Exception e) {}
        }
    }
}
