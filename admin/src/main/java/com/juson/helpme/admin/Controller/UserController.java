package com.juson.helpme.admin.Controller;

import com.juson.helpme.admin.Security.CurrentUser;
import com.juson.helpme.admin.Security.CustomUserDetails;
import com.juson.helpme.dao.User;
import com.juson.helpme.services.UserService;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 20/7/2016.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private Datastore datastore;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getList(Model model ,@CurrentUser CustomUserDetails userDetails) {

//        List<User> users = datastore.find(User.class).asList();

        List<User> users = userService.getUsers();

//        System.out.println("current user is "+userDetails.getUsername());

//        model.addAttribute("username",userDetails.getUsername());
        model.addAttribute("users", users);
        return "user/list";
    }


    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String getEdit(Model model, @RequestParam String id) {
//        User user = datastore.find(User.class).field("id").equal(new ObjectId(id)).get();

        User user = userService.getUserById(new ObjectId(id));

        model.addAttribute("user",user);
        modelAddGendersAndRoles(model);

        return "user/edit";
    }



    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String postEdit(Model model, @Valid User postUser, BindingResult bindingResult) {

        System.out.println("enter postEdit");


        if (userService.isExistMobile(postUser)) {
            bindingResult.rejectValue("mobile", "code", "已存在该手机号!");
        }

        if (userService.isExistEmail(postUser)) {
            bindingResult.rejectValue("email", "code", "已存在该邮箱!");
        }



        //global error
        if (postUser.getMobile().length() == 0 && postUser.getEmail().length() == 0) {
            bindingResult.rejectValue("", "code", "必须提供有效的手机号码或邮箱");
        }




        if (bindingResult.hasErrors()) {

            modelAddGendersAndRoles(model);

            return "user/edit";
        }


        userSetAttributeWithUserAndSave(postUser);


        return "redirect:/user/list";

    }




    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String deleteUser(Model model , @RequestParam String id) {

        User user = userService.getUserById(new ObjectId(id));

        userService.delete(user);

        return "redirect:/user/list";
    }


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String getCreateUser(Model model) {


        model.addAttribute("user",new User());

        modelAddGendersAndRoles(model);



        return "user/new";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String postCreateUser(Model model, @Valid User postUser, BindingResult bindingResult) {



        if (userService.isExistMobile(postUser)) {
            bindingResult.rejectValue("mobile", "code", "已存在该手机号!");
        }

        if (userService.isExistEmail(postUser)) {
            bindingResult.rejectValue("email", "code", "已存在该邮箱!");
        }



        if (postUser.getMobile().length() == 0 && postUser.getEmail().length() == 0) {
            bindingResult.rejectValue("", "code", "必须提供有效的手机号码或邮箱");
        }

        if (bindingResult.hasErrors()) {

            modelAddGendersAndRoles(model);
            return "user/new";
        }


        userSetAttributeWithUserAndSave(postUser);

        return "redirect:/user/list";
    }






    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String getUsersList(Model model,@RequestParam String name) {

//        List<User> users = datastore.find(User.class).asList();

        List<User> users = userService.getUsersByName(name);

        if (users == null) users = new ArrayList<>();

        System.out.println("getUsersList users size "+users.size());
        model.addAttribute("users", users);
        return "user/search";
    }




    private void modelAddGendersAndRoles(Model model){

        List<String> genders = new ArrayList();
        genders.add("男");
        genders.add("女");
        genders.add("保密");
        model.addAttribute("allGenders",genders);

        List<String> roles = new ArrayList();
        roles.add("管理员");
        roles.add("注册用户");
        roles.add("数据录入");
        model.addAttribute("allRoles",roles);

    }

    private void userSetAttributeWithUserAndSave(User postUser){

        User user = null;
        if (postUser.getId() != null)
            user = userService.getUserById(postUser.getId());

        if (user == null) user = new User();

        user.setWeight(postUser.getWeight());
        user.setGender(postUser.getGender());
        user.setBirthday(postUser.getBirthday());

        user.setName(postUser.getName());
        user.setMobile(postUser.getMobile());
        if (postUser.getMobile() == null || postUser.getMobile().equals(""))
            user.setMobile(null);

        user.setEmail(postUser.getEmail());
        if (postUser.getEmail() == null || postUser.getEmail().equals(""))
            user.setEmail("");

        userService.save(user);
        user.setRoles(postUser.getRoles());
        if(postUser.getId() == null) {
            user.setCreateDate(new Date());
            user.setRegisterDate(new Date());
            user.setLastUpdateDate(new Date());
        }
        userService.save(user);
    }

}
