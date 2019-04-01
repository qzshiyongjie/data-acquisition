package vip.firework.util;

import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注解工具类
 *
 * @author yongjieshi1
 * @date 2019/3/25 11:15 AM
 */
public class AnnoManageUtil {
    /**
     * 获取一个包底下的注解类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassUnderPackage(String packageName, Class clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(clazz);
        return classesList;
    }
}
