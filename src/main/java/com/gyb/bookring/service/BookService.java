package com.gyb.bookring.service;

import com.gyb.bookring.dao.BookDao;
import com.gyb.bookring.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    BookDao bookDao;

    public void add(Book book)throws Exception{
        bookDao.insert(book);
    }

    @Transactional
    public ArrayList delete(long[] bookIds,long userId) {
        List failedIds = new ArrayList<Long>();
        for (long bookId : bookIds) {
            try{
                bookDao.delete(bookId,userId);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                failedIds.add(bookId);
            }
        }
        return (ArrayList) failedIds;
    }

    public void update(Book book) throws Exception {
        bookDao.update(book);
    }

    public List<Book> list(long userId) throws Exception {
        return bookDao.list(userId);
    }

    public List<Book> listByName(String name) throws Exception{
        return bookDao.listByName(name);
    }


    public Book getByUserIdAndBookId(long userId, String bookId) throws Exception {
        return bookDao.getByUserIdAndBookId(userId,bookId);
    }

    public List<Book> listByNameAndUserId(String name, long userId)throws Exception {
        return bookDao.listByNameAndUserId(name, userId);
    }
}
