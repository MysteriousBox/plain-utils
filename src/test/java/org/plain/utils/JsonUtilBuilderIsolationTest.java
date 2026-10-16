package org.plain.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

/**
 * builder 配置的作用域隔离。
 * <p>
 * 历史上两个 builder 各自持有 {@code static ObjectMapper}，一个调用方注册模块或打开
 * default typing 会永久影响同 JVM 内所有后来的调用方——plain-authentication 全量跑测试时
 * 就因此让 Redis 实体写入路径抛 missing type id '@class'，而单跑却是绿的。
 * </p>
 */
@Slf4j
public class JsonUtilBuilderIsolationTest {

    /**
     * 只要注册成功就会改变 String 的输出形态，便于观察是否泄漏到别的 builder。
     */
    private static SimpleModule taggingModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new com.fasterxml.jackson.databind.JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("tagged:" + value);
            }
        });
        return module;
    }

    @Test
    public void registeredModuleAppliesToItsOwnBuilder() {
        String json = JsonUtil.serializeBuilder("abc").registerModule(taggingModule()).serialize();

        assert json != null && json.contains("tagged:abc") : "配置应在本 builder 内生效，实际：" + json;
    }

    @Test
    public void registeredModuleDoesNotLeakToOtherBuilder() {
        JsonUtil.serializeBuilder("abc").registerModule(taggingModule()).serialize();

        String json = JsonUtil.serializeBuilder("abc").serialize();

        assert "\"abc\"".equals(json) : "前一个 builder 的模块不应影响本 builder，实际：" + json;
    }

    @Test
    public void registeredModuleDoesNotLeakToStaticApi() {
        JsonUtil.serializeBuilder("abc").registerModule(taggingModule()).serialize();

        String json = JsonUtil.serialize("abc");

        assert "\"abc\"".equals(json) : "builder 的配置不应影响 JsonUtil 静态方法，实际：" + json;
    }

    @Test
    public void defaultTypingDoesNotLeakToOtherBuilder() {
        try {
            // 打开 default typing 后，读不带 @class 的普通 JSON 会失败——这正是历史上会波及其它调用方的现象
            JsonUtil.deserializeBuilder("{\"a\":\"b\"}").enableDefaultTyping()
                    .deserialize("{\"a\":\"b\"}", Map.class);
        } catch (RuntimeException expected) {
            log.info("expected failure inside the isolated builder: {}", expected.getMessage());
        }

        Map<String, Object> map = JsonUtil.deserializeBuilder("{\"a\":\"b\"}").deserialize("{\"a\":\"b\"}", Map.class);

        assert "b".equals(map.get("a")) : "别的 builder 不应被迫要求类型 ID，实际：" + map;
    }

    @Test
    public void staticDeserializeUnaffectedByBuilderTyping() throws JsonProcessingException {
        try {
            JsonUtil.deserializeBuilder("{\"a\":\"b\"}").enableDefaultTyping()
                    .deserialize("{\"a\":\"b\"}", Map.class);
        } catch (RuntimeException expected) {
            log.info("expected failure inside the isolated builder: {}", expected.getMessage());
        }

        Map<?, ?> map = JsonUtil.deserialize("{\"a\":\"b\"}", Map.class);

        assert "b".equals(map.get("a")) : "静态反序列化不应被 builder 配置影响，实际：" + map;
    }
}
