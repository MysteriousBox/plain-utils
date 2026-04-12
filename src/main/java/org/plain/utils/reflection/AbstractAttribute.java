package org.plain.utils.reflection;

@lombok.Getter
public abstract class AbstractAttribute {

    private String name;

    private Getter getter;

    @lombok.Setter
    private Setter setter;

    public abstract Class<?> getDeclaringClass();

    protected void setGetter(Getter getter){
        this.getter = getter;
    }

    protected void setName(String name){
        this.name = name;
    }

    public String getNameForStartWithLowerCase() {
        return getName().substring(0,1).toLowerCase().concat(getName().substring(1));
    }

}
