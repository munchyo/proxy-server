package io.chiho.proxyserver.controller;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import io.chiho.proxyserver.util.JsonUtil;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Getter
@PathPrefix("/api/test")
@RequiredArgsConstructor
public class SharedTestController {
    private final String serverId;

    @Get("/health")
    public HttpResponse healthCheck() {
        val healthCheckResponse = new HealthCheckResponse();
        healthCheckResponse.setIsHealth(true);
        healthCheckResponse.setServerId(serverId);

        val retStr = JsonUtil.toJsonStr(healthCheckResponse);

        return HttpResponse
            .builder()
            .ok()
            .content(MediaType.JSON_UTF_8, retStr)
            .build();
    }

    @Data
    public static class HealthCheckResponse {
        private String serverId;
        private Boolean isHealth;
    }
}
