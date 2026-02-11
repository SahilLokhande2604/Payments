package com.payment.controller;

import com.payment.dto.*;
import com.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService = null;

    @PostMapping("/create-order")
    public PaymentResponse createOrder(@RequestBody CreateOrderRequest request) throws Exception {
        return paymentService.createOrder(request);
    }

    @PostMapping("/verify")
    public PaymentResponse verifyPayment(@RequestBody PaymentVerificationRequest request) throws Exception {
        return paymentService.verifyPayment(request);
    }
}
