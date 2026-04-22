package org.plain.utils.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类扩展器，用于获取属性和方法元数据
 * @author Hugh
 */
public final class ClassExtender<T> {

    private final Class<T> aClass;

    private final List<Method> attributeMethods;

    private final List<AbstractAttribute> attributes = new ArrayList<>();

    private ClassExtender(Class<T> aClass) {
        this.aClass = aClass;
        //  过滤出 使用 public 修饰的 method
        this.attributeMethods = this.getAllDeclaredMethods().stream().filter(item-> Modifier.isPublic(item.getModifiers())).collect(Collectors.toList());
    }

    /**
     * 递归获取类声明的所有方法(包括父级类)元数据
     * @return method list
     */
    public List<Method> getAllDeclaredMethods(){
        return ReflectionUtil.getAllDeclaredMethods(this.aClass);
    }

    /**
     * 递归获取当前类和父类的方法
     * @param methodName 方法名
     * @param parameterType 方法参数类型
     * @return 方法
     */
    public Method getAllDeclaredMethod(String methodName,Class<?>... parameterType){
        return ReflectionUtil.getAllDeclaredMethod(this.aClass,methodName,parameterType);
    }


    /**
     * self 可以使用扩展目标的相关功能
     * @return class
     */
    public Class<T> getSelf() {
        return this.aClass;
    }

    public List<AbstractAttribute> getAttributes() {
        return this.attributes;
    }

    private void initAttributes() {
        for (Method method : attributeMethods) {
            AbstractGetter getter = GetterAdapter.adapterGetter(method);
            if (getter != null) {
                processGetter(getter);
                continue;
            }

            AbstractSetter setter = SetterAdapter.adapterSetter(method);
            if (setter != null) {
                processSetter(setter);
            }
        }
    }

    private void processGetter(AbstractGetter getter) {
        Optional<AbstractAttribute> attributeOptional = findAbstractAttribute(getter.getName());
        if (attributeOptional.isPresent()) {
            attributeOptional.get().setGetter(getter);
            return;
        }
        AbstractAttribute attribute = Attribute.create(getter.getName());
        attribute.setGetter(getter);
        attributes.add(attribute);
    }

    private void processSetter(AbstractSetter setter) {
        Optional<AbstractAttribute> attributeOptional = findAbstractAttribute(setter.getName());
        if (attributeOptional.isPresent()) {
            attributeOptional.get().setSetter(setter);
            return;
        }
        AbstractAttribute attribute = Attribute.create(setter.getName());
        attribute.setSetter(setter);
        attributes.add(attribute);
    }

    private Optional<AbstractAttribute> findAbstractAttribute(String name) {
        return attributes.stream().filter(item -> item.getName().equals(name))
                .findFirst();
    }

    public static <T> ClassExtender<T> create(Class<T> aClass){
        ClassExtender<T> tClassExtender = new ClassExtender<>(aClass);
        tClassExtender.initAttributes();
        return tClassExtender;
    }
}
