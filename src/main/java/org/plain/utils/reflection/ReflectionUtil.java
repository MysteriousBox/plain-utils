package org.plain.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 扩展 JDK 反射的工具类
 * @author Hugh
 */
public class ReflectionUtil {

    private ReflectionUtil() {
        throw new AssertionError("No instances");
    }

    /**
     * 递归获取所有字段（包括父类的字段）
     * @param clazz class
     * @return field
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        // 获取当前类的字段
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<>(Arrays.asList(declaredFields));
        // 如果父类存在，递归获取父类的字段
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            fields.addAll(getAllFields(superclass));
        }

        return fields;
    }

    public static List<AbstractGetter> getAllGetterAttributes(Class<?> aClass){
        ClassExtender<?> classExtender = ClassExtender.create(aClass);
        return classExtender.getAttributes().stream().map(AbstractAttribute::getGetter).collect(Collectors.toList());
    }

    /**
     * 递归获取类声明的所有方法(包括父级类)元数据
     * @param clazz c
     * @return method list
     */
    public static List<Method> getAllDeclaredMethods(Class<?> clazz){
        Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> result = new ArrayList<>(Arrays.asList(declaredMethods));
        Class<?> superclass = clazz.getSuperclass();
        if (superclass!=null && superclass != Object.class){
                 result.addAll(getAllDeclaredMethods(superclass));
            }
        
        return result;
    }


    /**
     * 根据方法名递归获取声明的方法(包括父类)
     * @param clazz clazz
     * @param methodName methodName
     * @param parameterTypes parameterTypes
     * @return Method
     */
    public static Method getAllDeclaredMethod(Class<?> clazz,String methodName,Class<?>... parameterTypes)  {
        Method declaredMethod = getDeclaredMethod(clazz, methodName, parameterTypes);
        if (declaredMethod!=null){
            return declaredMethod;
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass!=null){
            declaredMethod = getAllDeclaredMethod(superclass, methodName, parameterTypes);
        }
        return declaredMethod;
    }

    /**
     * 获取类声明的方法，并且如果找不到返回 null
     * @param clazz clazz
     * @param methodName methodName
     * @param parameterTypes parameterTypes
     * @return method
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ignored) {
            // 找不到方法时返回 null
        }
        return null;
    }
}
