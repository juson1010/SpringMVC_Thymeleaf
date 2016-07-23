package com.juson.helpme.admin.Controller;


import com.juson.helpme.dao.Role;
import com.juson.helpme.dao.User;
import com.juson.helpme.services.UserService;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by qianliang on 18/7/2016.
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    UserService userService;

    @Autowired
    Datastore datastore;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getHello(Model model) {
        model.addAttribute("hello", "Hello Please type in your name:");
        return "test/hello";
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.POST)
    public String welcome(Model model, @RequestParam String name) {

        model.addAttribute("welcome", "Welcome " + name);
        return "test/welcome";
    }

    @RequestMapping(value = "/dbInitUser", method = RequestMethod.GET)
    @ResponseBody
    public String dbInitUser() {
//
        User user = new User();
        user.setName("Admin");
        user.setMobile("18650481861");
        user.setBirthday(new Date());
        user.setLastUpdateDate(new Date());
        user.setRegisterDate(new Date());
        userService.save(user);
        userService.savePassword(user, "123456");


        return "User Created!";
    }
    @RequestMapping(value = "/dbInit", method = RequestMethod.GET)
    @ResponseBody
    public String dbInit() {
//
//        User user = new User();
//        user.setName("Tom");
//        user.setMobile("13612345678");
//        user.setCreateDate(new Date());
//        user.setRegisterDate(new Date());
//        user.setLastUpdateDate(new Date());
//
//
//
//        userService.save(user);

        Role role  = new Role();
        role.setName("管理员");
        datastore.save(role);

        Role role2 = new Role();
        role2.setName("注册用户");
        datastore.save(role2);

        Role role3 = new Role();
        role3.setName("数据录入");
        datastore.save(role3);

        return "User Created!";
    }
}
