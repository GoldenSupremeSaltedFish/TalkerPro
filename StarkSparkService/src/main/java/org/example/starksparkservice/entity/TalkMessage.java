package org.example.starksparkservice.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 这个类用于存储消息。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
@Setter
@Getter
public class TalkMessage {
    // Setters
    // Getters
    private String messageid;
    private String message;
    private int senderid;
    private int receiverid;

    public TalkMessage(String messageid, String message, int senderid, int receiverid) {
        this.messageid = messageid;
        this.message = message;
        this.senderid = senderid;
        this.receiverid = receiverid;
    }

}
