package io.chiho.proxyserver.config;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.endpoint.EndpointSelectionStrategy;
import com.linecorp.armeria.client.endpoint.healthcheck.HealthCheckedEndpointGroup;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.chiho.proxyserver.controller.AController;
import io.chiho.proxyserver.controller.BController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Configuration
public class ArmeriaConfig {
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(
        AController aController,
        BController bController
    ) throws ExecutionException, InterruptedException {
        final HealthCheckedEndpointGroup healthCheckedGroup =
            HealthCheckedEndpointGroup.builder(serverGroup, "/")
                .protocol(SessionProtocol.HTTP)
                .retryInterval(Duration.ofSeconds(10))
                .build();

        // Wait until the initial health check is finished.
        healthCheckedGroup.whenReady().get();
        // Customize the server using the given ServerBuilder. For example:
        return builder -> {
            // Add DocService that enables you to send Thrift and gRPC requests from web browser.
//            builder.serviceUnder("/docs", new DocService());

            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.
            builder.accessLogWriter(AccessLogWriter.combined(), false);

            // Add an Armeria annotated HTTP service.
            builder.annotatedService(aController);
            builder.annotatedService(bController);

            // You can also bind asynchronous RPC services such as Thrift and gRPC:
            // builder.service(THttpService.of(...));
            // builder.service(GrpcService.builder()...build());
        };
    }

    private static final EndpointGroup serverGroup = EndpointGroup.of(
        // You can use EndpointSelectionStrategy.weightedRoundRobin() or even
        // implement your own strategy to balance requests.
        EndpointSelectionStrategy.roundRobin(),
        Endpoint.of("127.0.0.1", 8081),
        Endpoint.of("127.0.0.1", 8082));

}
