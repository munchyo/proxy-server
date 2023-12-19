package io.chiho.proxyserver.controller;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import io.chiho.proxyserver.annotation.Controller;
import io.chiho.proxyserver.server.BServer;

@Controller
public class BController implements BServer {
    @Get()
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

    @Override
    public <T> Server newBackendServer(String prefix, T controller) {
        return null;
    }
}
