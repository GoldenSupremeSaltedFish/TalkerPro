package org.example.talker.controller;

import org.example.talker.annotation.JwtToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PictureController {
    @JwtToken
    @GetMapping("/picture")
    public String picture() {
        return "picture";
    }

}
