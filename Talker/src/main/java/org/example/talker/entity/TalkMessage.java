package org.example.talker.entity;

import lombok.Getter;
/**
 * 这个类用于存储消息。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
@Getter
public class TalkMessage {
    // Getters
    private String messageid;
    private String message;
    private String senderid;
    private String receiverid;

    public TalkMessage(String messageid, String message, String senderid, String receiverid) {
        this.messageid = messageid;
        this.message = message;
        this.senderid = senderid;
        this.receiverid = receiverid;
    }

    // Setters
    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }
}
