package com.gyb.bookring.dao;

import com.gyb.bookring.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    @Insert("insert into user(email,password) values (#{user.email},#{user.password})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id", keyColumn = "id")
    void signUp(@Param("user") User user) throws Exception;

    @Select("select * from user where email=#{user.email} and password=#{user.password}")
    User login(@Param("user") User user)throws Exception;

    @Delete("delete from user where id=#{user.id}")
    boolean delete(@Param("user") User user)throws Exception;

    @Update("update user set  email=#{user.email},password=#{user.password} where id=#{user.id}")
    void update(@Param("user") User user)throws Exception;

    @Select("select * from user where id = #{id}")
    User get(@Param("id") long id)throws Exception;

    @Select("select * from user where  email=#{email}")
    List<User> listByEmail(@Param("email") String email) throws Exception;
}
