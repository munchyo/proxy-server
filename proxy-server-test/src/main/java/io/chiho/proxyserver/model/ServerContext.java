package io.chiho.proxyserver.model;

import com.linecorp.armeria.server.Server;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;

@Builder
@Getter
public class ServerContext implements InitializingBean {
    private final String serverId;
    private final Server server;

    public int getServerPort() {
        return server.activeLocalPort();
    }

    @Override
    public void afterPropertiesSet() {
        server.start().join();
    }
}
