package org.plain.utils.reflection;

public class Attribute extends AbstractAttribute{

    public Attribute(String name){
        this.setName(name);
    }
    @Override
    public Class<?> getDeclaringClass() {
        if (getGetter()!=null){
            return getGetter().getDeclaringClass();
        }
        if (getSetter()!=null){
            return getSetter().getDeclaringClass();
        }
        return null;
    }


    public static AbstractAttribute create(String name){
        return new Attribute(name);
    }
}
