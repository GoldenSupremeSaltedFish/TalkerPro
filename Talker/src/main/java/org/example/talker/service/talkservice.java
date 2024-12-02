package org.example.talker.service;

import org.example.talker.entity.TalkMessage;

import java.util.concurrent.CompletableFuture;


public interface talkservice {

    String MessagetoMysql(TalkMessage talkMessage);

    TalkMessage MessagefromMysql(String messageid);

    boolean IsMessageRight(TalkMessage talkMessage);

    CompletableFuture<Void> MessagetoKafkaAsync(TalkMessage talkMessage);

    CompletableFuture<TalkMessage> MessagefromKafkaAsync(String messageid) throws InterruptedException;
    public String MessagetoKafka(TalkMessage talkMessage);

}
