package org.plain.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.plain.utils.reflection.AbstractGetter;
import org.plain.utils.reflection.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JSON 序列化 / 反序列化工具类
 * @author Hugh
 */
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() {
        throw new AssertionError("No instances");
    }

    public static String serialize(Object object){

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonUtilException(e);
        }
    }

    public static String serializeExclude(Object object,String... excludeAttributes){
        if (object instanceof Map){
            throw new  IllegalArgumentException("the object argument can not be map type!");
        }
        if (excludeAttributes.length ==0){
            throw new IllegalArgumentException("the length of the excludeAttributes array can not be 0!");
        }
        return serialize(mapAttribute(object, excludeAttributes,true));
    }

    public static String serializeInclude(Object object,String... includeAttributes){
        if (object instanceof Map){
            throw new  IllegalArgumentException("the object argument can be not map type!");
        }
        if (includeAttributes.length ==0){
            throw new IllegalArgumentException("the length of the includeAttributes array can not be 0!");
        }
        return serialize(mapAttribute(object, includeAttributes,false));
    }


    public static <T> T deserialize(String jsonStr,Class<T> clazz){
        try {
            return OBJECT_MAPPER.readValue(jsonStr,clazz);
        } catch (JsonProcessingException e) {
            throw new JsonUtilException(e);
        }
    }


    public static <T> T deserialize(String jsonStr, TypeReference<T> typeReference){
        try {
           return OBJECT_MAPPER.readValue(jsonStr,typeReference);
        } catch (JsonProcessingException e) {
            throw new JsonUtilException(e);
        }
    }


    public static <T> TypeReference<T> createTypeReference(){
        return new TypeReference<T>() {
        };
    }

    public static class JsonUtilException extends RuntimeException {
        public JsonUtilException(Throwable cause) {
            super(cause);
        }

        public JsonUtilException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static Map<String, Object> mapAttribute(Object object, String[] attributes, boolean excludeOrInclude) {
        Set<String> attributeSet = new HashSet<>(Arrays.asList(attributes));
        Map<String,Object> container = new HashMap<>(Math.max(attributeSet.size(), 1));
        Class<?> aClass = object.getClass();
        List<AbstractGetter> declaredFields = ReflectionUtil.getAllGetterAttributes(aClass);
        for (AbstractGetter getter : declaredFields) {
            String nameForStartWithLowerCase = getter.getNameForStartWithLowerCase();
            boolean contains = attributeSet.contains(nameForStartWithLowerCase);
            if (excludeOrInclude ? contains : !contains) {
                continue;
            }
            try {
                container.put(nameForStartWithLowerCase, getter.invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new JsonUtilException(e);
            }
        }
        return container;
    }




    /**
     * 序列化构建器
     * @param serializeObject 序列化对象
     * @return 构建器
     */
    public static SerializeBuilder serializeBuilder(Object serializeObject){
        return new SerializeBuilder(serializeObject);
    }

    public static SerializeBuilder serializeBuilder(){
        return new SerializeBuilder();
    }

    public static DeserializeBuilder deserializeBuilder(String jsonStr){
        return new DeserializeBuilder(jsonStr);
    }

    public static DeserializeBuilder deserializeBuilder(){
        return new DeserializeBuilder();
    }

    public static final class SerializeBuilder{
        /**
         * 只读模板。builder 的配置类方法（registerModule/s 等）会先 {@code copy()} 出实例私有副本，
         * 不再改写这份静态 mapper —— 否则一个调用方的模块注册会永久影响同 JVM 内所有后来的
         * builder（例如注册 OAuth2AuthorizationServerJackson2Module 会顺带打开 default typing，
         * 于是别的调用方读普通 Map 时开始报 missing type id '@class'）。
         * 未调用配置方法时直接复用模板，热路径不产生额外分配。
         */
        private static final ObjectMapper TEMPLATE_MAPPER = new ObjectMapper();

        private  Object serializeObject;

        private String[] attributes = new String[]{};

        private boolean excludeOrInclude;

        private ObjectMapper objectMapper;


        private SerializeBuilder(Object object){
            this.serializeObject = object;
        }

        private SerializeBuilder(){

        }

        private ObjectMapper writableObjectMapper(){
            if (this.objectMapper == null){
                this.objectMapper = TEMPLATE_MAPPER.copy();
            }
            return this.objectMapper;
        }

        private ObjectMapper readableObjectMapper(){
            return this.objectMapper != null ? this.objectMapper : TEMPLATE_MAPPER;
        }

        public SerializeBuilder registerModule(Module module){
            writableObjectMapper().registerModule(module);
            return this;
        }


        public SerializeBuilder registerModules(Module... modules){
            writableObjectMapper().registerModules(modules);
            return this;
        }

        public SerializeBuilder registerModules(Iterable<Module> modules){
            writableObjectMapper().registerModules(modules);
            return this;
        }



        public SerializeBuilder excludeAttributes(String... excludeAttributes){
            this.attributes = excludeAttributes;
            this.excludeOrInclude = true;
            return this;
        }

        public SerializeBuilder includeAttributes(String... includeAttribute){
            this.attributes = includeAttribute;
            this.excludeOrInclude = false;
            return this;
        }


        /**
         * 序列化
         * @return json str
         */
        public String serialize(){
            try {
                if (this.attributes.length >0){
                    return readableObjectMapper().writeValueAsString(mapAttribute(this.serializeObject, this.attributes, this.excludeOrInclude));
                }
                return readableObjectMapper().writeValueAsString(serializeObject);
            } catch (JsonProcessingException e) {
                throw new JsonUtilException(e.getMessage(),e);
            }
        }

        /**
         * 序列化
         * @param object 目标对象
         * @return json str
         */
        public String serialize(Object object){
            try {
                if (this.attributes.length >0){
                    return readableObjectMapper().writeValueAsString(mapAttribute(object, this.attributes, this.excludeOrInclude));
                }
                return readableObjectMapper().writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new JsonUtilException(e.getMessage(),e);
            }
        }
    }


    public static final class DeserializeBuilder{
        /**
         * 只读模板，配置方法只影响本 builder 的私有副本；理由见 {@code SerializeBuilder.TEMPLATE_MAPPER}。
         */
        private static final ObjectMapper TEMPLATE_MAPPER = new ObjectMapper();


        private  String jsonObject;

        private ObjectMapper objectMapper;


        public DeserializeBuilder(String jsonObject){
            this.jsonObject = jsonObject;
        }

        public DeserializeBuilder(){

        }

        private ObjectMapper writableObjectMapper(){
            if (this.objectMapper == null){
                this.objectMapper = TEMPLATE_MAPPER.copy();
            }
            return this.objectMapper;
        }

        private ObjectMapper readableObjectMapper(){
            return this.objectMapper != null ? this.objectMapper : TEMPLATE_MAPPER;
        }

        public DeserializeBuilder registerModule(Module module){
            writableObjectMapper().registerModule(module);
            return this;
        }

        public DeserializeBuilder enableDefaultTyping(){
            ObjectMapper mapper = writableObjectMapper();
            // 注意：默认 mapper 的校验器是放行的，这里等于沿用其现有强度，未做收紧
            mapper.activateDefaultTyping(
                    mapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, // 默认对非 final 类型启用类型信息
                    JsonTypeInfo.As.PROPERTY // 类型信息的注入方式
            );
            return this;
        }

        public DeserializeBuilder registerModules(Module... modules){
            writableObjectMapper().registerModules(modules);
            return this;
        }

        public DeserializeBuilder registerModules(Iterable<Module> modules){
            writableObjectMapper().registerModules(modules);
            return this;
        }

        public <T> T deserialize(Class<T> tClass){
            return deserialize(this.jsonObject,tClass);
        }


        public <T> T deserialize(String jsonStr,Class<T> clazz){
            try {
                return readableObjectMapper().readValue(jsonStr,clazz);
            } catch (JsonProcessingException e) {
                throw new JsonUtilException(e.getMessage(),e);
            }
        }

        public <T> T deserialize(TypeReference<T> typeReference){
            return deserialize(this.jsonObject,typeReference);
        }

        public <T> T deserialize(String jsonStr, TypeReference<T> typeReference){
            try {
                return readableObjectMapper().readValue(jsonStr,typeReference);
            } catch (JsonProcessingException e) {
                throw new JsonUtilException(e.getMessage(),e);
            }
        }
    }
}
