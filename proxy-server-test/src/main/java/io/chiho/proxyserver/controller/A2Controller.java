package io.chiho.proxyserver.controller;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import org.springframework.stereotype.Component;

@PathPrefix("/2")
@Component
public class A2Controller implements BaseAController {
    @Get("/health")
    public HttpResponse healthCheck() {
        return HttpResponse.of(200);
    }
}
