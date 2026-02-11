package com.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.payment.dto.CreateOrderRequest;
import com.payment.dto.PaymentResponse;
import com.payment.dto.PaymentVerificationRequest;
import com.payment.model.Payment;
import com.payment.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import com.razorpay.Utils;
import org.json.JSONObject;
import java.time.LocalDateTime;
// import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

        private final PaymentRepository paymentRepository;

        @Value("${razorpay.key}")
        private String key;

        @Value("${razorpay.secret}")
        private String secret;

        @Override
        public PaymentResponse createOrder(CreateOrderRequest request) throws Exception {

                RazorpayClient razorpayClient = new RazorpayClient(key, secret);

                JSONObject options = new JSONObject();
                options.put("amount", request.getAmount() * 100);
                options.put("currency", "INR");
                options.put("receipt", "receipt_" + System.currentTimeMillis());

                Order order = razorpayClient.orders.create(options);

                Payment payment = Payment.builder()
                                .policyId(request.getPolicyId())
                                .userId(request.getUserId())
                                .amount(request.getAmount())
                                .currency("INR")
                                .razorpayOrderId(order.get("id").toString())
                                .status("CREATED")
                                .createdAt(LocalDateTime.now())
                                .build();

                paymentRepository.save(payment);

                return PaymentResponse.builder()
                                .orderId(order.get("id").toString())
                                .status("CREATED")
                                .message("Order created successfully")
                                .build();
        }

        @Override
        public PaymentResponse verifyPayment(PaymentVerificationRequest request) throws Exception {

                Payment payment = paymentRepository
                                .findByRazorpayOrderId(request.getRazorpayOrderId())
                                .orElseThrow(() -> new RuntimeException("Order not found"));

                JSONObject attributes = new JSONObject();
                attributes.put("razorpay_order_id", request.getRazorpayOrderId());
                attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
                attributes.put("razorpay_signature", request.getRazorpaySignature());

                Utils.verifyPaymentSignature(attributes, secret);

                payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
                payment.setRazorpaySignature(request.getRazorpaySignature());
                payment.setStatus("SUCCESS");

                paymentRepository.save(payment);

                return PaymentResponse.builder()
                                .orderId(request.getRazorpayOrderId())
                                .status("SUCCESS")
                                .message("Payment verified successfully")
                                .build();
        }

        // @Override
        // public PaymentResponse createOrder(CreateOrderRequest request) throws
        // Exception {
        // // TODO Auto-generated method stub
        // return null;
        // }
}
