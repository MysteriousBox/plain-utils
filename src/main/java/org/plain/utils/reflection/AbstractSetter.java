package org.plain.utils.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * 属性 setter 的 metadata
 * @author Hugh
 */
public abstract class AbstractSetter {

    protected static final String SET_METHOD_PREFIX = "set";

    private String name;

    public String getName() {
        return this.name;
    }

    protected AbstractSetter() {
    }

    protected AbstractSetter(String name) {
        this.name = name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getNameForStartWithLowerCase() {
        return getName().substring(0, 1).toLowerCase().concat(getName().substring(1));
    }


    /**
     * 执行 setter 方法
     *
     * @param obj 目标对象
     * @param args 参数值
     * @throws InvocationTargetException when invocation fails
     * @throws IllegalAccessException when access is denied
     */
    public abstract void invoke(Object obj,Object... args) throws InvocationTargetException, IllegalAccessException;

    /**
     * 获取声明该 setter 的类
     *
     * @return declaring class
     */
    public abstract Class<?> getDeclaringClass();
}
