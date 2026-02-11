package com.payment.controller;

import com.payment.dto.CreateOrderRequest;
import com.payment.dto.PaymentResponse;
import com.payment.dto.PaymentVerificationRequest;
import com.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private  PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody CreateOrderRequest request) throws Exception {
        return ResponseEntity.ok(paymentService.createOrder(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestBody PaymentVerificationRequest request)
            throws Exception {
        return ResponseEntity.ok(paymentService.verifyPayment(request));
    }
}
