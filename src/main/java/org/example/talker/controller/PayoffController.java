package org.example.talker.controller;

import org.example.talker.annotation.JwtToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PayoffController {
    @PostMapping("/Pay")
    @JwtToken
    public void payoff(){

    }

}
