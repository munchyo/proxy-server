package io.chiho.proxyserver.server;

import com.linecorp.armeria.server.Server;

@FunctionalInterface
public interface BackendServer {
    <T> Server newBackendServer(String prefix, T controller);
}
