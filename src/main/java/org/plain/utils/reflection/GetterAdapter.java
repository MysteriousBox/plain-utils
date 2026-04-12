package org.plain.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetterAdapter extends Getter {

    private final Method method;

    public GetterAdapter(Method method) {
        this.method = method;
        this.setName(method.getName().substring(GET_METHOD_PREFIX.length()));
    }

    @Override
    public Object invoke(Object obj) throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(obj);
    }



    @Override
    public Class<?> getDeclaringClass() {
        return this.method.getDeclaringClass();
    }

    /**
     * 对方法进行设配，method 是否适配 attribute ,如果适配，则返回 一个Attribute的实例，否则返回null
     * @param method method
     * @return 适配则返回 一个 attribute 实例，否则返回 null
     */
    public static Getter adapterGetter(Method method){
        if (method.getReturnType().equals(void.class)){
            return null;
        }
        if (!isStartWithGet(method)){
            return null;
        }
        return new GetterAdapter(method);
    }



    public static Boolean isStartWithGet(Method method){
        return GET_METHOD_PREFIX.equals(method.getName().substring(0,GET_METHOD_PREFIX.length()));
    }


}
