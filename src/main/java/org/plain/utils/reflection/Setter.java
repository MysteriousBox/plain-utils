package org.plain.utils.reflection;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

/**
 * 属性 setter 的 metadata
 */
@Getter
public abstract class Setter {

    protected final static String SET_METHOD_PREFIX= "set";

    private String name;

    public Setter(){

    }

    public Setter(String name){
        this.name = name;
    }

    protected void setName(String name){
        this.name = name;
    }

    public String getNameForStartWithLowerCase() {
        return getName().substring(0,1).toLowerCase().concat(getName().substring(1));
    }


    public abstract void invoke(Object obj,Object... args) throws InvocationTargetException, IllegalAccessException;


    public abstract Class<?> getDeclaringClass();
}
