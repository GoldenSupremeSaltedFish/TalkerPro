package org.example.talker;

import jakarta.annotation.Resource;
import org.example.talker.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TalkerApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    private org.example.talker.mapper.MessageMapper MessageMapper;


    @Test
    void testmybatis() {
//        List<user> list = new ArrayList<>(10);
//        for (int i = 10; i < 20; i++) {
//            user user = new user(i, "name" + i, "email" + i, "password" + i);
//            list.add(user);
//        }
//
//        for (user user : list) {
//            userMapper.insertUser(user);
//        }
//        // 查询测试
//        for(int i = 0; i < 10; i++) {
//            user userList = userMapper.getUserById(i);
//            System.out.println("userList: " + userList);
//        }
    }

}
