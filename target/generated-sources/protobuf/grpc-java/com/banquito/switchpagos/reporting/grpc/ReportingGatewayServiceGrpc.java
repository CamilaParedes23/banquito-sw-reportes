package com.banquito.switchpagos.reporting.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.65.1)",
    comments = "Source: reporting_gateway.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReportingGatewayServiceGrpc {

  private ReportingGatewayServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "banquito.switchpagos.reporting.v1.ReportingGatewayService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse> getGetBatchSummaryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBatchSummary",
      requestType = com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.class,
      responseType = com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse> getGetBatchSummaryMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse> getGetBatchSummaryMethod;
    if ((getGetBatchSummaryMethod = ReportingGatewayServiceGrpc.getGetBatchSummaryMethod) == null) {
      synchronized (ReportingGatewayServiceGrpc.class) {
        if ((getGetBatchSummaryMethod = ReportingGatewayServiceGrpc.getGetBatchSummaryMethod) == null) {
          ReportingGatewayServiceGrpc.getGetBatchSummaryMethod = getGetBatchSummaryMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBatchSummary"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReportingGatewayServiceMethodDescriptorSupplier("GetBatchSummary"))
              .build();
        }
      }
    }
    return getGetBatchSummaryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getGetNoveltyReportMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNoveltyReport",
      requestType = com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.class,
      responseType = com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getGetNoveltyReportMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getGetNoveltyReportMethod;
    if ((getGetNoveltyReportMethod = ReportingGatewayServiceGrpc.getGetNoveltyReportMethod) == null) {
      synchronized (ReportingGatewayServiceGrpc.class) {
        if ((getGetNoveltyReportMethod = ReportingGatewayServiceGrpc.getGetNoveltyReportMethod) == null) {
          ReportingGatewayServiceGrpc.getGetNoveltyReportMethod = getGetNoveltyReportMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNoveltyReport"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReportingGatewayServiceMethodDescriptorSupplier("GetNoveltyReport"))
              .build();
        }
      }
    }
    return getGetNoveltyReportMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse> getGetCorporateReceiptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCorporateReceipt",
      requestType = com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.class,
      responseType = com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse> getGetCorporateReceiptMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse> getGetCorporateReceiptMethod;
    if ((getGetCorporateReceiptMethod = ReportingGatewayServiceGrpc.getGetCorporateReceiptMethod) == null) {
      synchronized (ReportingGatewayServiceGrpc.class) {
        if ((getGetCorporateReceiptMethod = ReportingGatewayServiceGrpc.getGetCorporateReceiptMethod) == null) {
          ReportingGatewayServiceGrpc.getGetCorporateReceiptMethod = getGetCorporateReceiptMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCorporateReceipt"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReportingGatewayServiceMethodDescriptorSupplier("GetCorporateReceipt"))
              .build();
        }
      }
    }
    return getGetCorporateReceiptMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getGetClearingFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetClearingFile",
      requestType = com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.class,
      responseType = com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
      com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getGetClearingFileMethod() {
    io.grpc.MethodDescriptor<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getGetClearingFileMethod;
    if ((getGetClearingFileMethod = ReportingGatewayServiceGrpc.getGetClearingFileMethod) == null) {
      synchronized (ReportingGatewayServiceGrpc.class) {
        if ((getGetClearingFileMethod = ReportingGatewayServiceGrpc.getGetClearingFileMethod) == null) {
          ReportingGatewayServiceGrpc.getGetClearingFileMethod = getGetClearingFileMethod =
              io.grpc.MethodDescriptor.<com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest, com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetClearingFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReportingGatewayServiceMethodDescriptorSupplier("GetClearingFile"))
              .build();
        }
      }
    }
    return getGetClearingFileMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReportingGatewayServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReportingGatewayServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReportingGatewayServiceStub>() {
        @java.lang.Override
        public ReportingGatewayServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReportingGatewayServiceStub(channel, callOptions);
        }
      };
    return ReportingGatewayServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReportingGatewayServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReportingGatewayServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReportingGatewayServiceBlockingStub>() {
        @java.lang.Override
        public ReportingGatewayServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReportingGatewayServiceBlockingStub(channel, callOptions);
        }
      };
    return ReportingGatewayServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReportingGatewayServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReportingGatewayServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReportingGatewayServiceFutureStub>() {
        @java.lang.Override
        public ReportingGatewayServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReportingGatewayServiceFutureStub(channel, callOptions);
        }
      };
    return ReportingGatewayServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getBatchSummary(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBatchSummaryMethod(), responseObserver);
    }

    /**
     */
    default void getNoveltyReport(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNoveltyReportMethod(), responseObserver);
    }

    /**
     */
    default void getCorporateReceipt(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCorporateReceiptMethod(), responseObserver);
    }

    /**
     */
    default void getClearingFile(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetClearingFileMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ReportingGatewayService.
   */
  public static abstract class ReportingGatewayServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ReportingGatewayServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ReportingGatewayService.
   */
  public static final class ReportingGatewayServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ReportingGatewayServiceStub> {
    private ReportingGatewayServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReportingGatewayServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReportingGatewayServiceStub(channel, callOptions);
    }

    /**
     */
    public void getBatchSummary(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBatchSummaryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNoveltyReport(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNoveltyReportMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCorporateReceipt(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCorporateReceiptMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getClearingFile(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request,
        io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetClearingFileMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ReportingGatewayService.
   */
  public static final class ReportingGatewayServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ReportingGatewayServiceBlockingStub> {
    private ReportingGatewayServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReportingGatewayServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReportingGatewayServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse getBatchSummary(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBatchSummaryMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse getNoveltyReport(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNoveltyReportMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse getCorporateReceipt(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCorporateReceiptMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse getClearingFile(com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetClearingFileMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ReportingGatewayService.
   */
  public static final class ReportingGatewayServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ReportingGatewayServiceFutureStub> {
    private ReportingGatewayServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReportingGatewayServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReportingGatewayServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse> getBatchSummary(
        com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBatchSummaryMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getNoveltyReport(
        com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNoveltyReportMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse> getCorporateReceipt(
        com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCorporateReceiptMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse> getClearingFile(
        com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetClearingFileMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_BATCH_SUMMARY = 0;
  private static final int METHODID_GET_NOVELTY_REPORT = 1;
  private static final int METHODID_GET_CORPORATE_RECEIPT = 2;
  private static final int METHODID_GET_CLEARING_FILE = 3;

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
        case METHODID_GET_BATCH_SUMMARY:
          serviceImpl.getBatchSummary((com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse>) responseObserver);
          break;
        case METHODID_GET_NOVELTY_REPORT:
          serviceImpl.getNoveltyReport((com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse>) responseObserver);
          break;
        case METHODID_GET_CORPORATE_RECEIPT:
          serviceImpl.getCorporateReceipt((com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse>) responseObserver);
          break;
        case METHODID_GET_CLEARING_FILE:
          serviceImpl.getClearingFile((com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse>) responseObserver);
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
          getGetBatchSummaryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
              com.banquito.switchpagos.reporting.grpc.BatchSummaryGrpcResponse>(
                service, METHODID_GET_BATCH_SUMMARY)))
        .addMethod(
          getGetNoveltyReportMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
              com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse>(
                service, METHODID_GET_NOVELTY_REPORT)))
        .addMethod(
          getGetCorporateReceiptMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
              com.banquito.switchpagos.reporting.grpc.CorporateReceiptGrpcResponse>(
                service, METHODID_GET_CORPORATE_RECEIPT)))
        .addMethod(
          getGetClearingFileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.banquito.switchpagos.reporting.grpc.ReportingBatchIdGrpcRequest,
              com.banquito.switchpagos.reporting.grpc.GeneratedFileGrpcResponse>(
                service, METHODID_GET_CLEARING_FILE)))
        .build();
  }

  private static abstract class ReportingGatewayServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReportingGatewayServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.banquito.switchpagos.reporting.grpc.ReportingGatewayProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReportingGatewayService");
    }
  }

  private static final class ReportingGatewayServiceFileDescriptorSupplier
      extends ReportingGatewayServiceBaseDescriptorSupplier {
    ReportingGatewayServiceFileDescriptorSupplier() {}
  }

  private static final class ReportingGatewayServiceMethodDescriptorSupplier
      extends ReportingGatewayServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ReportingGatewayServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ReportingGatewayServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReportingGatewayServiceFileDescriptorSupplier())
              .addMethod(getGetBatchSummaryMethod())
              .addMethod(getGetNoveltyReportMethod())
              .addMethod(getGetCorporateReceiptMethod())
              .addMethod(getGetClearingFileMethod())
              .build();
        }
      }
    }
    return result;
  }
}
