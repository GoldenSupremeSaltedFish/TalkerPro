package org.example.talker.util.Impl;



/**
 * 这个类是AES加密解密的实现类，用于实现AES加密解密的功能。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-16
 *
 */

public class AESUtilImpl implements AESUtil {
    private static final String SECRET_KEY = "your-secret-key-at-least-32-bytes-long";
    private static final int GCM_IV_LENGTH = 16;
    private static final int GCM_TAG_LENGTH = 128;

    public  String decrypt(String encryptedData) throws Exception {
//        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
//
//        // 从密文中提取 IV
//        ByteBuffer buffer = ByteBuffer.wrap(decodedData);
//        byte[] iv = new byte[GCM_IV_LENGTH];
//        buffer.get(iv);
//        byte[] ciphertext = new byte[buffer.remaining()];
//        buffer.get(ciphertext);
//
//        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
//        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
//
//        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
//        byte[] decryptedData = cipher.doFinal(ciphertext);
//
//        return new String(decryptedData);
        return encryptedData;
    }
}
