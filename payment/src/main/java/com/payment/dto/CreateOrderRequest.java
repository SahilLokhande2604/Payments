package com.payment.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private Long policyId;
    private Long userId;
    private Double amount;
}
