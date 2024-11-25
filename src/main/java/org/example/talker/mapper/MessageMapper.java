package org.example.talker.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.talker.entity.TalkMessage;
//TODO
// 可切换的数据源-例如mongodb
@Mapper
public interface MessageMapper {
    @Select("SELECT * FROM messages WHERE messageid = #{messageid}")
    TalkMessage getMessageById(String messageid);

    @Insert("INSERT INTO messages (messageid, message, senderid, receiverid) VALUES (#{messageid}, #{message}, #{senderid}, #{receiverid})")
    void insertMessage(TalkMessage message);

    @Delete("DELETE FROM messages WHERE messageid = #{messageid}")
    void deleteMessageById(String messageid);
}
