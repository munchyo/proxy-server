package io.chiho.proxyserver.config;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.endpoint.EndpointSelectionStrategy;
import com.linecorp.armeria.client.endpoint.healthcheck.HealthCheckedEndpointGroup;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.AbstractHttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

final class ProxyConfig extends AbstractHttpService {
    private static final String viaHeaderValue = "HTTP/2.0 Armeria proxy";

    private static final EndpointGroup serverGroup = EndpointGroup.of(
        EndpointSelectionStrategy.roundRobin(),
        Endpoint.of("127.0.0.1", 8081),
        Endpoint.of("127.0.0.1", 8082)
    );

    private final WebClient loadBalancingClient;

    private final boolean addForwardedToRequestHeaders;
    private final boolean addViaToResponseHeaders;

    ProxyConfig() throws ExecutionException, InterruptedException {
        this(newLoadBalancingClient());
    }

    ProxyConfig(WebClient loadBalancingClient) {
        this.loadBalancingClient = loadBalancingClient;
        addForwardedToRequestHeaders = true;
        addViaToResponseHeaders = true;
    }

    private static WebClient newLoadBalancingClient() throws ExecutionException, InterruptedException {
        final HealthCheckedEndpointGroup healthCheckedGroup =
            HealthCheckedEndpointGroup.builder(serverGroup, "/internal/l7check")
                .protocol(SessionProtocol.HTTP)
                .retryInterval(Duration.ofSeconds(10))
                .build();

        healthCheckedGroup.whenReady().get();

        return WebClient.builder(SessionProtocol.HTTP, healthCheckedGroup)
            .responseTimeoutMillis(0)
            .decorator(LoggingClient.newDecorator())
            .build();
    }

    @Override
    protected HttpResponse doGet(
        ServiceRequestContext ctx,
        HttpRequest req
    ) {
        if (addForwardedToRequestHeaders) {
            req = addForwarded(ctx, req);
            ctx.updateRequest(req);
        }

        final HttpResponse res = loadBalancingClient.execute(req);
        if (addViaToResponseHeaders) {
            return addViaHeader(res);
        }
        return res;
    }

    private static HttpRequest addForwarded(
        ServiceRequestContext ctx,
        HttpRequest req
    ) {
        final StringBuilder sb = new StringBuilder();
        sb.append("for: ").append(ctx.remoteAddress().getAddress().getHostAddress());
        sb.append(", host: ").append(req.authority());
        sb.append(", proto: ").append(ctx.sessionProtocol());

        return req.withHeaders(req.headers().toBuilder()
            .add(HttpHeaderNames.FORWARDED, sb.toString()));
    }

    private static HttpResponse addViaHeader(HttpResponse res) {
        return res.mapHeaders(
            headers -> headers.toBuilder()
                .add(HttpHeaderNames.VIA, viaHeaderValue)
                .build()
        );
    }
}
