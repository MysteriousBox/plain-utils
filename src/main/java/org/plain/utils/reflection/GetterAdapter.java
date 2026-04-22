package org.plain.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Getter 方法适配器
 * @author Hugh
 */
public class GetterAdapter extends AbstractGetter {

    private final Method method;

    protected GetterAdapter(Method method) {
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
    public static AbstractGetter adapterGetter(Method method){
        if (method.getReturnType().equals(void.class)){
            return null;
        }
        if (!isStartWithGet(method)){
            return null;
        }
        return new GetterAdapter(method);
    }



    public static boolean isStartWithGet(Method method) {
        return GET_METHOD_PREFIX.equals(method.getName().substring(0, GET_METHOD_PREFIX.length()));
    }


}
