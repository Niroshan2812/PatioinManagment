package com.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    // override the existing billingAccount method with own logic
    // Stream observer multiple response and communication helper -- replace REST
    // how we open connection and how we connect to communication
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("CreateBillingAccount request received {}", billingRequest.toString());

        // business logic = save db, perform cal


        //dummy
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();

        // send response grpc to client then end the cycle
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}
