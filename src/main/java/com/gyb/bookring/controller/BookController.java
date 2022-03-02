package com.gyb.bookring.controller;

import com.gyb.bookring.entity.Book;
import com.gyb.bookring.entity.OptionBook;
import com.gyb.bookring.entity.Result;
import com.gyb.bookring.entity.User;
import com.gyb.bookring.service.BookService;
import com.gyb.bookring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;
    private String unHandelFailedResponse = "Failed, please contact administator";

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Map<String, Object> optionParam) {
        try {
            User verifyUser = JSON.parseObject(JSON.toJSONString(optionParam.get("user")), User.class);
            Book optionBook = JSON.parseObject(JSON.toJSONString(optionParam.get("book")), Book.class);
            User verifiedUser = userService.login(verifyUser);
            if (verifiedUser != null) {
                List<Book> sameNameBooks = bookService.listByNameAndUserId(optionBook.getName(),verifiedUser.getId());
                if (sameNameBooks.size() > 0) {
                    return new Result("Book add failed, already have this book in Bookring");
                } else {
                    optionBook.setUserId(verifiedUser.getId());
                    bookService.add(optionBook);
                    return new Result(optionBook);
                }
            }else{
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(unHandelFailedResponse);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody Map<String, Object> optionParam) {
        try {
            User verifyUser = JSON.parseObject(JSON.toJSONString(optionParam.get("user")), User.class);
            String deleteIdsS = JSON.toJSONString(optionParam.get("deleteIds"));
            User verifiedUser = userService.login(verifyUser);
            if (verifiedUser != null) {
                long[] deleteIds = Arrays.stream(deleteIdsS.split(",")).mapToLong(Long::parseLong).toArray();
                List failedIds = bookService.delete(deleteIds,verifiedUser.getId());
                if (failedIds.size() > 0) {
                    return new Result(false, "same books delete failed", JSON.toJSONString(failedIds));
                } else {
                    return new Result();
                }
            }else{
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(unHandelFailedResponse);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody Map<String, Object> optionParam) {
        try {
            User verifyUser = JSON.parseObject(JSON.toJSONString(optionParam.get("user")), User.class);
            Book optionBook = JSON.parseObject(JSON.toJSONString(optionParam.get("book")), Book.class);
            User verifiedUser = userService.login(verifyUser);
            if (verifiedUser != null) {
                try {
                    optionBook.setUserId(verifiedUser.getId());
                    if(optionBook.getId()==0){
                        bookService.add(optionBook);
                    }else{
                        bookService.update(optionBook);
                    }
                    return new Result();
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result(unHandelFailedResponse);
                }
            }else{
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(unHandelFailedResponse);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result list(@RequestBody Map<String, Object> optionParam) {
        try {
            User verifyUser = JSON.parseObject(JSON.toJSONString(optionParam.get("user")), User.class);
            User verifiedUser = userService.login(verifyUser);
            if (verifiedUser != null) {
                try {
                    List<Book> bookList = bookService.list(verifiedUser.getId());
                    return new Result(bookList);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result(unHandelFailedResponse);
                }
            }else{
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(unHandelFailedResponse);
        }
    }

    @RequestMapping(value = "/pageGet", method = RequestMethod.POST)
    public Result pageGet(@RequestBody Map<String, Object> optionParam) {
        try {
            User verifyUser = JSON.parseObject(JSON.toJSONString(optionParam.get("user")), User.class);
            String bookId = JSON.toJSONString(optionParam.get("bookId"));
            User verifiedUser = userService.login(verifyUser);
            if (verifiedUser != null) {
                try {
                    Book book = bookService.getByUserIdAndBookId(verifiedUser.getId(),bookId);
                    if(book!=null){
                        if("pdf".equals(book.getType())){
                            return new Result(true,"",book.getPage());
                        }else if("epub".equals(book.getType())){
                            return new Result(true,"",book.getPage()+"-"+book.getCfi());
                        }else{
                            return new Result("Book type error!");
                        }
                    }else{
                        return new Result("Book not found!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result(unHandelFailedResponse);
                }
            }else{
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(unHandelFailedResponse);
        }
    }


}
