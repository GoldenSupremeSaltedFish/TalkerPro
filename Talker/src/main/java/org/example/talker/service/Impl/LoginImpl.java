package org.example.talker.service.Impl;

import jakarta.annotation.Resource;

import org.example.talker.mapper.UserMapper;
import org.example.talker.service.Login;
import org.example.talker.util.Impl.AESUtil;
import org.example.talker.util.Impl.AESUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 这个类用于实现用户登录功能。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
@Service
public class LoginImpl implements Login {

    private AESUtil aesUtil;

    @Resource

    private UserMapper userMapper;


    @Override
    public String loginforpassword(String Dname, String Dpassword) throws Exception {
        //todo 解密
//         String realname=aesUtil.decrypt(Dname);
        String realname=Dname;
        //todo 解密
//         String realpassword=aesUtil.decrypt(Dpassword);
        String realpassword=Dpassword;
        String sqlpassword =userMapper.getPasswordByName(realname);
        String role=userMapper.getRoleByName(realname);
        if(realpassword.equals(sqlpassword))
        {
            return role;
        }
        else
        {
            return null;
        }

    }

    @Override
    public String loginforEmail(String Demail, String Dpassword) throws Exception {
        //todo 解密
//         String realemail=aesUtil.decrypt(Demail);
        String realemail=Demail;
        String realpassword=aesUtil.decrypt(Dpassword);
        String sqlpassword =userMapper.getPasswordByEmail(realemail);
        String role=userMapper.getRoleByEmail(realemail);
        if(realpassword.equals(Dpassword))
        {
            return role;
        }
        else
        {
            return null;
        }

    }

    @Override
    public Boolean logforWechat() {
        return false;
    }


}
