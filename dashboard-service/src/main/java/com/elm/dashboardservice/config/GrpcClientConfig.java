package com.elm.dashboardservice.config;

import com.elm.proto.DashboardServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.client.ingestion-service.address}")
    private String grpcServerAddress;

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder
                .forTarget(grpcServerAddress.replace("static://", ""))
                .usePlaintext()
                .build();
    }

    @Bean
    public DashboardServiceGrpc.DashboardServiceBlockingStub dashboardBlockingStub(ManagedChannel channel) {
        return DashboardServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    public DashboardServiceGrpc.DashboardServiceStub dashboardAsyncStub(ManagedChannel channel) {
        return DashboardServiceGrpc.newStub(channel);
    }
}