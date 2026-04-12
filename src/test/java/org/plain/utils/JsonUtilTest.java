package org.plain.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class JsonUtilTest {

    @Test
    void serialize() {
        Map<String,Object> map = new HashMap<>();
        map.put("code","123456");
        String json = JsonUtil.serialize(map);
        assert json != null;
        log.info("json str serialize result:{}",json);
    }

    @Test
    void deserialize() {
        String json = "{\"code\":\"1234556\"}";
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        Map<String, Object> deserialize = JsonUtil.deserialize(json, typeRef);
        assert deserialize !=null;
        log.info("json deserialized result:{}",deserialize);
    }
}