package com.gyb.bookring.dao;

import com.gyb.bookring.entity.Book;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BookDao {
    @Insert("insert into book(user_id,name,classify,all_page,net_url_type,net_url,last_read_time) values (#{book.userId},#{book.name},#{book.classify},#{book.allPage},#{book.netUrlType},#{book.netUrl},#{book.lastReadTime})")
    @Options(useGeneratedKeys = true, keyProperty = "book.id", keyColumn = "id")
    void insert(@Param("book") Book book)throws Exception;

    @Delete("delete from book where id = #{bookId} and user_id = #{userId}")
    void delete(@Param("bookId") long bookId,@Param("userId") long userId)throws Exception;

    @Update("update book set classify=#{book.classify},page=#{book.page},net_url_type=#{book.netUrlType},net_url=#{book.netUrl},last_read_time=#{book.lastReadTime} where id=#{book.id}")
    void update(@Param("book") Book book)throws Exception;

    @Select("select * from book where id=#{bookId}")
    Book get(@Param("bookId") long bookId)throws Exception;

    @Select("select * from book where user_id = #{userId}")
    List<Book> list(@Param("userId") long userId)throws Exception;

    @Select("select * from book where name = #{name}")
    List<Book> listByName(@Param("name") String name) throws Exception;
}