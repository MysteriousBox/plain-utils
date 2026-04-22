package org.plain.utils.reflection;

/**
 * 抽象属性定义
 * @author Hugh
 */
public abstract class AbstractAttribute {

    private String name;

    private AbstractGetter getter;

    private AbstractSetter setter;

    public String getName() {
        return this.name;
    }

    public AbstractGetter getGetter() {
        return this.getter;
    }

    public AbstractSetter getSetter() {
        return this.setter;
    }

    /**
     * 获取声明该属性的类
     *
     * @return declaring class of the property
     */
    public abstract Class<?> getDeclaringClass();

    protected void setGetter(AbstractGetter getter) {
        this.getter = getter;
    }

    protected void setSetter(AbstractSetter setter) {
        this.setter = setter;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getNameForStartWithLowerCase() {
        return getName().substring(0,1).toLowerCase().concat(getName().substring(1));
    }

}
