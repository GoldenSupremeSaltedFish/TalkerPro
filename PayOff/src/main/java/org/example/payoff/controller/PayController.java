package org.example.payoff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public String pay() {
        return paymentService.processPayment();
    }
}
