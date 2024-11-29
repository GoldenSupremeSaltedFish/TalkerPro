package org.example.payoff.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/21
 * @time 下午11:51
 */
@RestController
public class PayController {
    @PostMapping("/pay")
    public String pay() {
        return "Payment completed";
    }

}
