package com.payment.service;

import com.payment.dto.CreateOrderRequest;
import com.payment.dto.PaymentResponse;
import com.payment.dto.PaymentVerificationRequest;

public interface PaymentService {

    PaymentResponse createOrder(CreateOrderRequest request) throws Exception;

    PaymentResponse verifyPayment(PaymentVerificationRequest request) throws Exception;
}
