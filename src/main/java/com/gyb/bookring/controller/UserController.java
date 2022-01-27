package com.gyb.bookring.controller;

import com.alibaba.fastjson.JSON;
import com.gyb.bookring.entity.Book;
import com.gyb.bookring.entity.Result;
import com.gyb.bookring.entity.User;
import com.gyb.bookring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public Result userSignUp(User user) {
        try {
            Result verifyRes = userService.verify(user);
            if (verifyRes.isStatus()) {
                userService.signUp(user);
                return new Result();
            } else {
                return new Result(verifyRes.getReason());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Sign up failed,please contact administrator!");
        }
    }

    @RequestMapping(value = "/logIn", method = RequestMethod.POST)
    public Result userLogIn(User user) {
        try {
            User currentUser = userService.login(user);
            if (currentUser != null) {
                return new Result();
            } else {
                return new Result("Log in failed, please check your input info again!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Log in failed, please contact administrator!");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result userUpdate(User user) {
        try {
            User verifyUser = new User();
            verifyUser.setEmail(user.getEmail().split(",")[0]);
            verifyUser.setPassword(user.getPassword().split(",")[0]);
            User optionUser = userService.login(verifyUser);
            if (optionUser != null) {
                String newEmail = user.getEmail().split(",")[1];
                String newPassword = user.getPassword().split(",")[1];
                if (!"".equals(newEmail)) {
                    optionUser.setEmail(newEmail);
                }
                if (!"".equals(newPassword)) {
                    optionUser.setPassword(newPassword);
                }
                userService.update(optionUser);
                return new Result();
            } else {
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Update account info failed, please contact administrator!");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result userDelete(User user) {
        try {
            User deleteUser = userService.login(user);
            if (deleteUser != null) {
                userService.delete(deleteUser);
                return new Result();
            } else {
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Delete account failed, please contact administrator!");
        }
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public Result feedback(@RequestBody Map<String, Object> optionParam) {
        try {
            User verifyUser = JSON.parseObject(JSON.toJSONString(optionParam.get("user")), User.class);
            User verifiedUser = userService.login(verifyUser);
            String feedbackString = JSON.toJSONString(optionParam.get("feedback"));
            if (verifiedUser != null) {
                userService.feedback(verifiedUser.getId(), feedbackString);
                return new Result();
            } else {
                return new Result("Not found your account info!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Feedback failed, please contact administrator!");
        }
    }
}
