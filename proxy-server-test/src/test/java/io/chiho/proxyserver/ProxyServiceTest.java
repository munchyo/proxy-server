package io.chiho.proxyserver;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpStatus;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProxyServiceTest {

    @Test
    @DisplayName("프록시 서버를 거칠 때도 값이 불변하는가")
    void proxyTest() {
        val webClient = WebClient.of();
        val proxyServerResponse = webClient.get("http://127.0.0.1:8080/a").aggregate().join();
        val aServerResponse = webClient.get("http://127.0.0.1:8081/a").aggregate().join();
        Assertions.assertEquals(proxyServerResponse.status(), HttpStatus.OK);
        Assertions.assertEquals(aServerResponse.status(), HttpStatus.OK);
    }

}
