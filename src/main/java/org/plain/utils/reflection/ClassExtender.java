package org.plain.utils.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ClassExtender<T> {

    private final Class<T> aClass;

    private final List<Method> attributeMethods;

    @lombok.Getter
    private final List<AbstractAttribute> attributes = new ArrayList<>();

    private ClassExtender(Class<T> aClass){
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
    public Class<T> getSelf(){
        return this.aClass;
    }

    private void initAttributes() {
        for (Method method : attributeMethods) {
            // 尝试适配 getter
            Getter getter = GetterAdapter.adapterGetter(method);
            if (getter != null){
                Optional<AbstractAttribute> attributeOptional = findAbstractAttribute(getter.getName());
                if (attributeOptional.isPresent()){
                    AbstractAttribute attribute = attributeOptional.get();
                    attribute.setGetter(getter);
                    continue;
                }
                AbstractAttribute attribute = Attribute.create(getter.getName());
                attribute.setGetter(getter);
                attributes.add(attribute);
                continue;
            }

            // 尝试适配 setter
            Setter setter = SetterAdapter.adapterSetter(method);
            if (setter != null){
                Optional<AbstractAttribute> attributeOptional = findAbstractAttribute(setter.getName());
                if (attributeOptional.isPresent()){
                    AbstractAttribute attribute = attributeOptional.get();
                    attribute.setSetter(setter);
                    continue;
                }
                AbstractAttribute attribute = Attribute.create(setter.getName());
                attribute.setSetter(setter);
                attributes.add(attribute);
            }
        }
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
