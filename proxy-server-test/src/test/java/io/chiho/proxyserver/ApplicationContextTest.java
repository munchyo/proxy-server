package io.chiho.proxyserver;

import io.chiho.proxyserver.server.BackendServer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootTest
class ApplicationContextTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void 빈갯수테스트() {
        val beansOfType = applicationContext.getBeansOfType(BackendServer.class);
        int size = beansOfType.size();
        Assertions.assertEquals(2, size);
    }
}
