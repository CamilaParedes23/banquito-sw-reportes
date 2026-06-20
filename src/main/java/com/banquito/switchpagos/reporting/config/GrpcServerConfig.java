package com.banquito.switchpagos.reporting.config;

import com.banquito.switchpagos.reporting.grpc.GrpcReportingGatewayService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcServerConfig.class);

    private final Integer grpcPort;
    private final GrpcReportingGatewayService grpcReportingGatewayService;
    private Server server;

    public GrpcServerConfig(
            @Value("${grpc.server.port}") Integer grpcPort,
            GrpcReportingGatewayService grpcReportingGatewayService) {
        this.grpcPort = grpcPort;
        this.grpcReportingGatewayService = grpcReportingGatewayService;
    }

    @PostConstruct
    public void start() throws IOException {
        server = ServerBuilder.forPort(grpcPort)
                .addService(grpcReportingGatewayService)
                .build()
                .start();
        LOG.info("reporting-service gRPC server started on port {}", grpcPort);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
            LOG.info("reporting-service gRPC server stopped");
        }
    }
}
