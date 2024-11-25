package org.example.talker.mapper;

import org.apache.ibatis.annotations.*;
import org.example.talker.entity.user;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    user getUserById(int id);

    @Select("SELECT * FROM user WHERE name = #{name}")
    user getUserByName(String name);

    @Select("SELECT * FROM user WHERE email = #{email}")
    user getUserByEmail(String email);

    @Insert("INSERT INTO user (id, name, email, password) VALUES (#{id}, #{name}, #{email}, #{password})")
    void insertUser(user user);

    @Update("UPDATE user SET name = #{name}, email = #{email}, password = #{password} WHERE id = #{id}")
    void updateUser(user user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUserById(int id);

    @Select("SELECT password FROM user WHERE id = #{id}")
    String getPasswordById(int id);

    @Select("SELECT password FROM user WHERE name = #{name}")
    String getPasswordByName(String name);

    @Select("SELECT password FROM user WHERE email = #{email}")
    String getPasswordByEmail(String email);

    @Select("SELECT Role FROM user WHERE id = #{id}")
    String getRowByID(int id);

    @Select("SELECT Role FROM user WHERE name = #{name}")
    String getRoleByName(String name);

    @Select("SELECT Role FROM user WHERE email = #{name}")
    String getRoleByEmail(String email);
}