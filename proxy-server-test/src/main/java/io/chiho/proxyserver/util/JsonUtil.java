package io.chiho.proxyserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chiho.proxyserver.controller.SharedTestController;
import lombok.SneakyThrows;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {}

    @SneakyThrows
    public static String toJsonStr(
        Object param
    ) {
        return mapper.writeValueAsString(param);
    }

    @SneakyThrows
    public static SharedTestController.HealthCheckResponse toHealthCheckResponse(
        String jsonStr
    ) {
        return mapper.readValue(jsonStr, SharedTestController.HealthCheckResponse.class);
    }
}
