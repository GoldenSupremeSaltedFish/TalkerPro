package org.example.talker.kafka;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/12/1
 * @time 上午2:19
 */
public interface LinkMessageRouter {
    public void inputConnection(String connectionurl, String userId);
    public void outputConnection(String connectionurl, String userId);
    public void shutDown();
    public String getUrlByMessageId(String connectionurl,String message);
}
