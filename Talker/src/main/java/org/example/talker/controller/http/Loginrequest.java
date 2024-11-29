package org.example.talker.controller.http;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class Loginrequest implements Serializable {
    String token;
    String username;
    public Loginrequest(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
