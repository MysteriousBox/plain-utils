package org.plain.utils.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * JDK反射的一个扩展, 在 Java 1.8 中没有明确属性的概念，这里定义了属性的 getter
 * @author Hugh
 */
public abstract class AbstractGetter {

    protected static final String GET_METHOD_PREFIX = "get";


    /**
     * 属性的名称 开头大写
     */
    private String name;

    public String getName() {
        return this.name;
    }

    protected AbstractGetter() {
    }

    protected AbstractGetter(String name) {
        this.name = name;
    }

    public String getNameForStartWithLowerCase() {
        return getName().substring(0,1).toLowerCase().concat(getName().substring(1));
    }

    protected void setName(String name){
        this.name = name;
    }

    /**
     * 执行 getter 方法并返回值
     *
     * @param obj target object
     * @return getter value
     * @throws InvocationTargetException when invocation fails
     * @throws IllegalAccessException when access is denied
     */
    public abstract Object invoke(Object obj) throws InvocationTargetException, IllegalAccessException;

    /**
     * 获取声明该 getter 的类
     *
     * @return declaring class
     */
    public abstract Class<?> getDeclaringClass();
}
