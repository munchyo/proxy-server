package io.chiho.proxyserver.config;

import com.linecorp.armeria.server.Server;
import io.chiho.proxyserver.controller.BaseAController;
import io.chiho.proxyserver.controller.SharedTestController;
import io.chiho.proxyserver.model.ServerContext;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ServerAConfig {
    @Bean("serverAContext")
    public ServerContext serverAContext(
        ApplicationContext applicationContext
    ) {
        val id = UUID.randomUUID().toString();

        val serverBuilder = Server
            .builder()
            .http(0)
            .annotatedService(
                new SharedTestController(id)
            );

        applicationContext
            .getBeansOfType(BaseAController.class)
            .forEach((key, service) -> {
                serverBuilder.annotatedService(service);
            });

        val server = serverBuilder.build();

        return ServerContext
            .builder()
            .serverId(id)
            .server(server)
            .build();
    }
}
