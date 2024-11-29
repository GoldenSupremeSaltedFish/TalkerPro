package org.example.talker.util.Impl;

import org.springframework.stereotype.Component;

@Component
public interface AESUtil {
      String decrypt(String encryptedData)throws Exception;
}
