package io.chiho.proxyserver.controller;

import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.PathPrefix;
import org.springframework.stereotype.Component;

@Component
@PathPrefix("/b")
public class BController {
    @Get("/")
    public String defaultHello() {
        return "Hello, world! This message is from B server";
    }

    @Get("/hello/{name}")
    public String hello(
        @Param String name
    ) {
        return String.format(
            "Hello, %s! This message is from B server!",
            name
        );
    }
}
