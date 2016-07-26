package com.juson.springMVC.Controller;

import com.juson.helpme.dao.User;
import com.juson.helpme.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cqx on 16/7/26.
 */
@RestController
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "hello" ,method = RequestMethod.GET)
    public String sayHello(){

        return "hello world !";
    }

    @RequestMapping(value = "user" ,method = RequestMethod.GET)
    public User getUser(){

        User user = userService.getUserByMobile("18650481862");

        return user;

    }
}
