package io.chiho.proxyserver.config;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.logging.LoggingService;
import io.chiho.proxyserver.annotation.Controller;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArmeriaConfig {
    private final ApplicationContext applicationContext;
    private int port = 8081;

    @Bean
    private void init() {
//        applicationContext.getBeansWithAnnotation(Controller.class)
//            .forEach((name, value) -> newBackendServer(
//                    getPathPrefix(name),
//                    value
//                ).start().join()
//            );
//
//        final Server proxyServer = newProxyServer();
//
//
//        proxyServer.closeOnJvmShutdown(() -> {
////            backend1.stop().join();
////            backend2.stop().join();
//
//            proxyServer.stop().join();
//            log.info("The proxy server has been stopped.");
//        });
//
//        proxyServer.start().join();
    }

    @SneakyThrows
    Server newProxyServer() {
        return Server.builder()
            .http(8080)
            .tlsSelfSigned()
            // Disable timeout to serve infinite streaming response.
            .requestTimeoutMillis(0)
            .serviceUnder("/", new ProxyConfig())
            .decorator(LoggingService.newDecorator())
            .build();
    }

    @SneakyThrows
    <T> Server newBackendServer(String serverPathPrefix, T server) {
        return Server.builder()
            .http(port++)
            .requestTimeoutMillis(0)
            .annotatedService(serverPathPrefix, server)
            .service("/internal/l7check", HealthCheckService.of())
            .build();
    }

    private String getPathPrefix(String input) {
        return "/".concat(
            String.valueOf(
                input.charAt(0)
            ).toLowerCase()
        );
    }
}