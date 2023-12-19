package io.chiho.proxyserver;

import com.linecorp.armeria.client.WebClient;
import io.chiho.proxyserver.model.ServerContext;
import io.chiho.proxyserver.util.JsonUtil;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;

@SpringBootTest
public class ReverseProxyTest {
    private static ServerContext serverAContext;
    private static WebClient webClient;

    @BeforeAll
    public static void init(
        @Qualifier("serverAContext") ServerContext serverAContext
    ) {
        ReverseProxyTest.serverAContext = serverAContext;
        webClient = WebClient.of("http://localhost:8080");
    }

    @Test
    public void serverAHealthCheckTest() {
        val response = webClient
            .get("/api/test/health")
            .aggregate()
            .join();

        val result = JsonUtil.toHealthCheckResponse(response.contentUtf8());

        Assertions.assertEquals(200, response.status().code());
        Assertions.assertEquals(serverAContext.getServerId(), result.getServerId());
    }
}
