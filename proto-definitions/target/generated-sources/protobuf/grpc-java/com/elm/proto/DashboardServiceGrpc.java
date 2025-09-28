package com.elm.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: ingestion.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DashboardServiceGrpc {

  private DashboardServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.elm.DashboardService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.elm.proto.DashboardRequest,
      com.elm.proto.DashboardOverview> getGetDashboardOverviewMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDashboardOverview",
      requestType = com.elm.proto.DashboardRequest.class,
      responseType = com.elm.proto.DashboardOverview.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.elm.proto.DashboardRequest,
      com.elm.proto.DashboardOverview> getGetDashboardOverviewMethod() {
    io.grpc.MethodDescriptor<com.elm.proto.DashboardRequest, com.elm.proto.DashboardOverview> getGetDashboardOverviewMethod;
    if ((getGetDashboardOverviewMethod = DashboardServiceGrpc.getGetDashboardOverviewMethod) == null) {
      synchronized (DashboardServiceGrpc.class) {
        if ((getGetDashboardOverviewMethod = DashboardServiceGrpc.getGetDashboardOverviewMethod) == null) {
          DashboardServiceGrpc.getGetDashboardOverviewMethod = getGetDashboardOverviewMethod =
              io.grpc.MethodDescriptor.<com.elm.proto.DashboardRequest, com.elm.proto.DashboardOverview>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDashboardOverview"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.elm.proto.DashboardRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.elm.proto.DashboardOverview.getDefaultInstance()))
              .setSchemaDescriptor(new DashboardServiceMethodDescriptorSupplier("GetDashboardOverview"))
              .build();
        }
      }
    }
    return getGetDashboardOverviewMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.elm.proto.DashboardRequest,
      com.elm.proto.TodayActivityCharts> getGetTodayActivityChartsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTodayActivityCharts",
      requestType = com.elm.proto.DashboardRequest.class,
      responseType = com.elm.proto.TodayActivityCharts.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.elm.proto.DashboardRequest,
      com.elm.proto.TodayActivityCharts> getGetTodayActivityChartsMethod() {
    io.grpc.MethodDescriptor<com.elm.proto.DashboardRequest, com.elm.proto.TodayActivityCharts> getGetTodayActivityChartsMethod;
    if ((getGetTodayActivityChartsMethod = DashboardServiceGrpc.getGetTodayActivityChartsMethod) == null) {
      synchronized (DashboardServiceGrpc.class) {
        if ((getGetTodayActivityChartsMethod = DashboardServiceGrpc.getGetTodayActivityChartsMethod) == null) {
          DashboardServiceGrpc.getGetTodayActivityChartsMethod = getGetTodayActivityChartsMethod =
              io.grpc.MethodDescriptor.<com.elm.proto.DashboardRequest, com.elm.proto.TodayActivityCharts>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTodayActivityCharts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.elm.proto.DashboardRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.elm.proto.TodayActivityCharts.getDefaultInstance()))
              .setSchemaDescriptor(new DashboardServiceMethodDescriptorSupplier("GetTodayActivityCharts"))
              .build();
        }
      }
    }
    return getGetTodayActivityChartsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.elm.proto.WeeklyRequest,
      com.elm.proto.WeeklyTrends> getGetWeeklyTrendsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetWeeklyTrends",
      requestType = com.elm.proto.WeeklyRequest.class,
      responseType = com.elm.proto.WeeklyTrends.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.elm.proto.WeeklyRequest,
      com.elm.proto.WeeklyTrends> getGetWeeklyTrendsMethod() {
    io.grpc.MethodDescriptor<com.elm.proto.WeeklyRequest, com.elm.proto.WeeklyTrends> getGetWeeklyTrendsMethod;
    if ((getGetWeeklyTrendsMethod = DashboardServiceGrpc.getGetWeeklyTrendsMethod) == null) {
      synchronized (DashboardServiceGrpc.class) {
        if ((getGetWeeklyTrendsMethod = DashboardServiceGrpc.getGetWeeklyTrendsMethod) == null) {
          DashboardServiceGrpc.getGetWeeklyTrendsMethod = getGetWeeklyTrendsMethod =
              io.grpc.MethodDescriptor.<com.elm.proto.WeeklyRequest, com.elm.proto.WeeklyTrends>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetWeeklyTrends"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.elm.proto.WeeklyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.elm.proto.WeeklyTrends.getDefaultInstance()))
              .setSchemaDescriptor(new DashboardServiceMethodDescriptorSupplier("GetWeeklyTrends"))
              .build();
        }
      }
    }
    return getGetWeeklyTrendsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DashboardServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DashboardServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DashboardServiceStub>() {
        @java.lang.Override
        public DashboardServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DashboardServiceStub(channel, callOptions);
        }
      };
    return DashboardServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DashboardServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DashboardServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DashboardServiceBlockingStub>() {
        @java.lang.Override
        public DashboardServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DashboardServiceBlockingStub(channel, callOptions);
        }
      };
    return DashboardServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DashboardServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DashboardServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DashboardServiceFutureStub>() {
        @java.lang.Override
        public DashboardServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DashboardServiceFutureStub(channel, callOptions);
        }
      };
    return DashboardServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * 1. Main Dashboard View - Real-time overview
     * </pre>
     */
    default void getDashboardOverview(com.elm.proto.DashboardRequest request,
        io.grpc.stub.StreamObserver<com.elm.proto.DashboardOverview> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDashboardOverviewMethod(), responseObserver);
    }

    /**
     * <pre>
     * 2. Today's Activity Breakdown - Charts data
     * </pre>
     */
    default void getTodayActivityCharts(com.elm.proto.DashboardRequest request,
        io.grpc.stub.StreamObserver<com.elm.proto.TodayActivityCharts> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTodayActivityChartsMethod(), responseObserver);
    }

    /**
     * <pre>
     * 3. Weekly Trends
     * </pre>
     */
    default void getWeeklyTrends(com.elm.proto.WeeklyRequest request,
        io.grpc.stub.StreamObserver<com.elm.proto.WeeklyTrends> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetWeeklyTrendsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DashboardService.
   */
  public static abstract class DashboardServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DashboardServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DashboardService.
   */
  public static final class DashboardServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DashboardServiceStub> {
    private DashboardServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DashboardServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DashboardServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. Main Dashboard View - Real-time overview
     * </pre>
     */
    public void getDashboardOverview(com.elm.proto.DashboardRequest request,
        io.grpc.stub.StreamObserver<com.elm.proto.DashboardOverview> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDashboardOverviewMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 2. Today's Activity Breakdown - Charts data
     * </pre>
     */
    public void getTodayActivityCharts(com.elm.proto.DashboardRequest request,
        io.grpc.stub.StreamObserver<com.elm.proto.TodayActivityCharts> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTodayActivityChartsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 3. Weekly Trends
     * </pre>
     */
    public void getWeeklyTrends(com.elm.proto.WeeklyRequest request,
        io.grpc.stub.StreamObserver<com.elm.proto.WeeklyTrends> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetWeeklyTrendsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DashboardService.
   */
  public static final class DashboardServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DashboardServiceBlockingStub> {
    private DashboardServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DashboardServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DashboardServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. Main Dashboard View - Real-time overview
     * </pre>
     */
    public com.elm.proto.DashboardOverview getDashboardOverview(com.elm.proto.DashboardRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDashboardOverviewMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 2. Today's Activity Breakdown - Charts data
     * </pre>
     */
    public com.elm.proto.TodayActivityCharts getTodayActivityCharts(com.elm.proto.DashboardRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTodayActivityChartsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 3. Weekly Trends
     * </pre>
     */
    public com.elm.proto.WeeklyTrends getWeeklyTrends(com.elm.proto.WeeklyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetWeeklyTrendsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DashboardService.
   */
  public static final class DashboardServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DashboardServiceFutureStub> {
    private DashboardServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DashboardServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DashboardServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. Main Dashboard View - Real-time overview
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.elm.proto.DashboardOverview> getDashboardOverview(
        com.elm.proto.DashboardRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDashboardOverviewMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 2. Today's Activity Breakdown - Charts data
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.elm.proto.TodayActivityCharts> getTodayActivityCharts(
        com.elm.proto.DashboardRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTodayActivityChartsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 3. Weekly Trends
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.elm.proto.WeeklyTrends> getWeeklyTrends(
        com.elm.proto.WeeklyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetWeeklyTrendsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DASHBOARD_OVERVIEW = 0;
  private static final int METHODID_GET_TODAY_ACTIVITY_CHARTS = 1;
  private static final int METHODID_GET_WEEKLY_TRENDS = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_DASHBOARD_OVERVIEW:
          serviceImpl.getDashboardOverview((com.elm.proto.DashboardRequest) request,
              (io.grpc.stub.StreamObserver<com.elm.proto.DashboardOverview>) responseObserver);
          break;
        case METHODID_GET_TODAY_ACTIVITY_CHARTS:
          serviceImpl.getTodayActivityCharts((com.elm.proto.DashboardRequest) request,
              (io.grpc.stub.StreamObserver<com.elm.proto.TodayActivityCharts>) responseObserver);
          break;
        case METHODID_GET_WEEKLY_TRENDS:
          serviceImpl.getWeeklyTrends((com.elm.proto.WeeklyRequest) request,
              (io.grpc.stub.StreamObserver<com.elm.proto.WeeklyTrends>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetDashboardOverviewMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.elm.proto.DashboardRequest,
              com.elm.proto.DashboardOverview>(
                service, METHODID_GET_DASHBOARD_OVERVIEW)))
        .addMethod(
          getGetTodayActivityChartsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.elm.proto.DashboardRequest,
              com.elm.proto.TodayActivityCharts>(
                service, METHODID_GET_TODAY_ACTIVITY_CHARTS)))
        .addMethod(
          getGetWeeklyTrendsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.elm.proto.WeeklyRequest,
              com.elm.proto.WeeklyTrends>(
                service, METHODID_GET_WEEKLY_TRENDS)))
        .build();
  }

  private static abstract class DashboardServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DashboardServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.elm.proto.DashboardProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DashboardService");
    }
  }

  private static final class DashboardServiceFileDescriptorSupplier
      extends DashboardServiceBaseDescriptorSupplier {
    DashboardServiceFileDescriptorSupplier() {}
  }

  private static final class DashboardServiceMethodDescriptorSupplier
      extends DashboardServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DashboardServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DashboardServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DashboardServiceFileDescriptorSupplier())
              .addMethod(getGetDashboardOverviewMethod())
              .addMethod(getGetTodayActivityChartsMethod())
              .addMethod(getGetWeeklyTrendsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
