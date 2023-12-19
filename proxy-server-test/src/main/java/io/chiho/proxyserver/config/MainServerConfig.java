package io.chiho.proxyserver.config;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.endpoint.EndpointSelectionStrategy;
import com.linecorp.armeria.client.endpoint.healthcheck.HealthCheckedEndpointGroup;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.AbstractHttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.chiho.proxyserver.model.ServerContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class MainServerConfig extends AbstractHttpService {

    private EndpointGroup endpointGroup;
    private final List<Endpoint> endpoints = new ArrayList<>();

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(
        ApplicationContext applicationContext
    ) {
        setEndpoint(applicationContext);

//        newLoadBalancingClient();

        return serverBuilder -> {
            serverBuilder.http(8080)
                .tlsSelfSigned()
                .requestTimeoutMillis(0)
                .decorator(LoggingService.newDecorator())
                .build()
                .start()
                .join();
        };
    }

//    @SneakyThrows
//    private WebClient newLoadBalancingClient() {
//        final HealthCheckedEndpointGroup healthCheckedGroup =
//            HealthCheckedEndpointGroup.builder(endpointGroup, "/a/api/test/health")
//                .protocol(SessionProtocol.HTTP)
//                .retryInterval(Duration.ofSeconds(10))
//                .build();
//
//        healthCheckedGroup.whenReady().get();
//
//        return WebClient.builder(SessionProtocol.HTTP, healthCheckedGroup)
//            .responseTimeoutMillis(0)
//            .decorator(LoggingClient.newDecorator())
//            .build();
//    }

    private void setEndpoint(ApplicationContext applicationContext) {
        applicationContext
            .getBeansOfType(ServerContext.class)
            .forEach((k, v) -> {
                log.info("=== server info - id: {}, port: {}", v.getServerId(), v.getServerPort());
                endpoints.add(
                    Endpoint.of(
                        "localhost",
                        v.getServerPort()
                    )
                );
            });
        endpointGroup = EndpointGroup.of(
            EndpointSelectionStrategy.roundRobin(),
            endpoints
        );
    }

    @Override
    protected HttpResponse doGet(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return super.doGet(ctx, req);
    }
}
