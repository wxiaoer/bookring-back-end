package com.gyb.bookring.controller;

import com.gyb.bookring.entity.Result;
import com.gyb.bookring.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public String userActions() {
        return "index";
    }
}
