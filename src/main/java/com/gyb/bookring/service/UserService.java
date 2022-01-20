package com.gyb.bookring.service;

import com.gyb.bookring.dao.UserDao;
import com.gyb.bookring.entity.Result;
import com.gyb.bookring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public void signUp(User user) throws Exception {
        userDao.signUp(user);
    }

    public User login(User user) throws Exception {
        return userDao.login(user);
    }

    public void update(User user) throws Exception {
        userDao.update(user);
    }

    public User get(long id) throws Exception {
        return userDao.get(id);
    }

    public Result verify(User user) throws Exception {
        if (userDao.listByEmail(user.getEmail()).size() > 0) {
            return new Result("User already exists");
        }
        if (user.getEmail() == null || user.getPassword() == null ||  "".equals(user.getEmail()) || "".equals(user.getPassword())) {
            return new Result("Sign up failed, please check your input info again.");
        }
        if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", user.getEmail())
                && Pattern.matches("^[a-zA-Z]\\w{5,17}$", user.getPassword())) {
            return new Result();
        } else {
            return new Result("Sign up failed, please check your input info again.");
        }
    }

    public void delete(User user) throws Exception {
        userDao.delete(user);
    }


}
