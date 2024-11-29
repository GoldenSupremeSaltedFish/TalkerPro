package org.example.talker.controller.http;

import lombok.Getter;

public class Loginpost {
    @Getter
    String way;
    @Getter
    String password=null;
    @Getter
    String name=null;
    @Getter
    String email=null;
    public Loginpost(String way, String password, String name, String email) {
        this.way = way;
        this.password = password;
        this.name = name;
        this.email = email;

    }
}
