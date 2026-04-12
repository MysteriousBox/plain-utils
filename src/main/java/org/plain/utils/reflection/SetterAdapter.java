package org.plain.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetterAdapter extends Setter{

    private final Method method;

    public SetterAdapter(Method method){
        this.method = method;
        this.setName(this.method.getName().substring(SET_METHOD_PREFIX.length()));
    }

    @Override
    public void invoke(Object obj, Object... args) throws InvocationTargetException, IllegalAccessException {
        this.method.invoke(obj,args);
    }

    @Override
    public Class<?> getDeclaringClass() {
        return this.method.getDeclaringClass();
    }


    /**
     * 对方法进行设配，method 是否适配 Setter ,如果适配，则返回 一个Setter的实例，否则返回null
     * @param method method
     * @return 适配则返回 一个 Setter 实例，否则返回 null
     */
    public static Setter adapterSetter(Method method){
        if (!method.getReturnType().equals(void.class)){
            return null;
        }
        if (!isStartWithSet(method)){
            return null;
        }
        return new SetterAdapter(method);
    }



    public static Boolean isStartWithSet(Method method){
        return SET_METHOD_PREFIX.equals(method.getName().substring(0,SET_METHOD_PREFIX.length()));
    }

}
