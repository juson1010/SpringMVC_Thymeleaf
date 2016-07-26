package com.juson.helpme.admin.Controller;

import com.juson.helpme.QueryUserBean;
import com.juson.helpme.admin.Security.CurrentUser;
import com.juson.helpme.admin.Security.CustomUserDetails;
import com.juson.helpme.dao.User;
import com.juson.helpme.services.UserService;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
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
    private UserService userService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getList(Model model ,@CurrentUser CustomUserDetails userDetails
    ,@RequestParam(required = false) String action
    ,@RequestParam(required = false) String gender
    ,@RequestParam(required = false) String mobile_email_name
            ,@RequestParam(required = false) String offset
            ,@RequestParam(required = false) String role
                          ) {

        QueryUserBean userBean = new QueryUserBean();

        if (offset == null || offset.length() == 0) offset = "0";
        userBean.setOffset(Integer.parseInt(offset));
        if ("next".equals(action)){
            userBean.setOffset(userBean.getOffset() + userBean.getLimit());

        }else if ("pre".equals(action)){
            if (userBean.getOffset() - userBean.getLimit() <= 0) userBean.setOffset(0);
            else
                userBean.setOffset(userBean.getOffset() - userBean.getLimit());
        }else if ("search".equals(action)){
            userBean.setOffset(0);
        }

        userBean.setCurPage((userBean.getOffset()/userBean.getLimit()) + 1);
        userBean.setRole(role);
        userBean.setLimit(10);
        userBean.setMobile_email_name(mobile_email_name);
        userBean.setGender(gender);


        List<User> users = userService.getUsersByUserBean(userBean);

        model.addAttribute("queryUserBean",userBean);
        model.addAttribute("users", users);

        model.addAttribute("curPage",userBean.getCurPage());
        model.addAttribute("allPages",userBean.getAllPages());

        List<String> genders = new ArrayList();
        genders.add("请选择");
        genders.add("男");
        genders.add("女");
        genders.add("保密");

        model.addAttribute("allGenders",genders);

        List<String> roles = new ArrayList();
        roles.add("未归类");
        roles.add("管理员");
        roles.add("注册用户");
        roles.add("数据录入");
        model.addAttribute("allRoles",roles);

        model.addAttribute("curUserId",userDetails.getUser().getId());

        return "user/list";
    }


    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String getEdit(Model model, @RequestParam String id
            ,@RequestParam(required = false) String list_gender
            ,@RequestParam(required = false,defaultValue = "") String mobile_email_name
            ,@RequestParam(required = false,defaultValue = "0") String offset
            ,@RequestParam(required = false) String role) {

        User user = userService.getUserById(new ObjectId(id));
        model.addAttribute("user",user);

        String searchQueryStr = "gender="+list_gender+"&mobile_email_name="+mobile_email_name
                +"&offset="+offset+"&role="+role;

        model.addAttribute("searchQueryStr",searchQueryStr);

        modelAddGendersAndRoles(model);

        return "user/edit";
    }



    /*可改进,将所有参数拼接成一个searchQueryStr*/
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String postEdit(Model model, @Valid User postUser, BindingResult bindingResult
            ,@RequestParam(required = false) String searchQueryStr) {

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
            return "user/edit?"+searchQueryStr;
        }

        userSetAttributeWithUserAndSave(postUser);
        return "redirect:/user/list?"+searchQueryStr;

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




//
//
//    @RequestMapping(value = "search", method = RequestMethod.GET)
//    public String getUsersList(Model model,@RequestParam(required = false) String name) {
//
//        List<User> users = datastore.find(User.class).asList();
//
//
//        if (users == null) users = new ArrayList<>();
//
//        System.out.println("getUsersList users size "+users.size());
//        model.addAttribute("users", users);
//        modelAddGendersAndRoles(model);
//        return "user/list";
//    }
//



//    private List<User> getUsersByUserBean(QueryUserBean userBean){
//
//        if (userBean == null) userBean = new QueryUserBean();
//
//
//        Query<User> query = datastore.find(User.class);
//        if (userBean.getGender() != null && !userBean.getGender().equals("请选择"))
//            query.field("gender").equal(userBean.getGender());
//
//        if (userBean.getEmail() != null)
//            query.field("email").containsIgnoreCase(userBean.getEmail());
//
//        if (userBean.getMobile() != null)
//            query.field("mobile").containsIgnoreCase(userBean.getMobile());
//
//        if (userBean.getName() != null)
//            query.field("name").containsIgnoreCase(userBean.getName());
//
//
//        if (userBean.getMobile_email_name() != null) {
//            String search = userBean.getMobile_email_name();
//            query.or(query.criteria("mobile").containsIgnoreCase(search),query.criteria("email").containsIgnoreCase(search),
//                    query.criteria("name").containsIgnoreCase(search));
//        }
//
//
//
//        query.order("-registerDate");
//        int size = query.asList().size();
//        int allPages = (size + 9)/ userBean.getLimit();
//        userBean.setAllPages(allPages);
//
//        query.offset(userBean.getOffset()).limit(userBean.getLimit());
//
//
//        return query.asList();
//
//    }
}
