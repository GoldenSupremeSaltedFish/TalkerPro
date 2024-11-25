package org.example.talker.service;

public interface Login {
    String loginforpassword(String Dname, String Dpassword) throws Exception;

    String loginforEmail(String Demail, String Dpassword) throws Exception;

    Boolean logforWechat();

}
