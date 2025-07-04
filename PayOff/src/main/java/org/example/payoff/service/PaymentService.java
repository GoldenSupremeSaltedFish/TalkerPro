package org.example.payoff.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String processPayment() {
        // 模拟支付逻辑
        System.out.println("Processing payment...");
        return "Payment completed successfully";
    }
}