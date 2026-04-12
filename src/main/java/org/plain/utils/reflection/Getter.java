package org.plain.utils.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * JDK反射的一个扩展, 在 Java 1.8中没有明确属性的概念，这里定义了属性的 getter
 */
@lombok.Getter
public abstract class Getter {

    protected final static String GET_METHOD_PREFIX= "get";


    /**
     * 属性的名称 开头大写
     */
    private String name;


    public Getter(){

    }

    public Getter(String name){
        this.name = name;
    }

    public String getNameForStartWithLowerCase() {
        return getName().substring(0,1).toLowerCase().concat(getName().substring(1));
    }

    protected void setName(String name){
        this.name = name;
    }

    public abstract Object invoke(Object obj) throws InvocationTargetException, IllegalAccessException;


    public abstract Class<?> getDeclaringClass();
}
