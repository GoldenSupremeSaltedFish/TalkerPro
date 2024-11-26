package org.example.talker.service;

import org.example.talker.entity.TalkMessage;


public interface talkservice {
    String MessagetoRedis(TalkMessage talkMessage);

    TalkMessage MessagefromRedis(String messageid);

    String MessagetoMysql(TalkMessage talkMessage);

    TalkMessage MessagefromMysql(String messageid);

    boolean IsMessageRight(TalkMessage talkMessage);

    String MessagetoKafka(TalkMessage talkMessage);

    TalkMessage MessagefromKafka(String messageid);

}
