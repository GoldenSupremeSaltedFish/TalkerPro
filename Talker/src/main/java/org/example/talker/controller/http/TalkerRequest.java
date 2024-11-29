package org.example.talker.controller.http;

import lombok.Getter;

import lombok.Setter;

@Setter
@Getter
public class TalkerRequest {
    // getter 和 setter 方法

    private String body;
    private int senderId;
    private int receiverId;

}
