package org.example.talker.controller.http;

import lombok.Getter;
import lombok.Setter;

public class UserRequest {
    @Getter
    @Setter
    private String email=null;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String name=null;

    UserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


}
