package io.chiho.proxyserver;

import com.linecorp.armeria.client.WebClient;
import io.chiho.proxyserver.model.ServerContext;
import io.chiho.proxyserver.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;

@Slf4j
@SpringBootTest
public class ServerARequestTest {
    private static ServerContext serverContext;
    private static WebClient webClient;

    @BeforeAll
    public static void init(
        @Qualifier("serverAContext") ServerContext serverContext
    ) {
        ServerARequestTest.serverContext = serverContext;

        val url = MessageFormat.format(
            "http://localhost:{0}/api/test",
            String.valueOf(serverContext.getServerPort())
        );

        webClient = WebClient.of(url);
    }

    @Test
    public void healthCheckTest() {
        val response = webClient
            .get("/health")
            .aggregate()
            .join();

        val result = JsonUtil.toHealthCheckResponse(response.contentUtf8());

        Assertions.assertEquals(200, response.status().code());
        Assertions.assertEquals(serverContext.getServerId(), result.getServerId());
    }
}
