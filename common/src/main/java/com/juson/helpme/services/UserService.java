package com.juson.helpme.services;

import com.juson.helpme.QueryUserBean;
import com.juson.helpme.dao.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 19/7/2016.
 */
@Service
public class UserService {

    @Autowired
    private Datastore datastore;



    public void save(User user) {

        List roles = user.getRoles();
        /**
         * 每次保存对 用户roles进行删除并重新保存。确保改选项增删正确
         * */
        delete(user);

        System.out.println("roles size "+roles.size());
        for (int i = 0; i < roles.size(); i++) {
            User_Role user_Role = new User_Role();
            Role roleQ = datastore.find(Role.class).field("name").equal(roles.get(i) + "").get();
            user_Role.setRoleId(roleQ.getId());
            user_Role.setUserId(user.getId());

            datastore.save(user_Role);

        }
        if (user.getPassword() == null || user.getPassword().length() == 0){

            savePassword(user, "123456");
        }
        datastore.save(user);

    }

    public void delete(User user){
    /*删除用户同时,删除其与Role之间的关联,即删除中间表User_Role对应项*/
        List<User_Role> user_Roles = datastore.find(User_Role.class).field("userId").equal(user.getId()).asList();

        for (int i = 0;i < user_Roles.size();i++){
            datastore.delete(user_Roles.get(i));
        }
        datastore.delete(user);
    }

    public User getUserById(ObjectId id){
        User user = datastore.find(User.class).field("id").equal(id).get();


        List roles = datastore.find(User_Role.class).field("userId").equal(id).asList();

        List rolesStr = new ArrayList();

        for (int i = 0; i < roles.size(); i++) {
            User_Role user_role =(User_Role) roles.get(i);

            Role role =  datastore.find(Role.class).field("id").equal(user_role.getRoleId()).get();

            rolesStr.add(role.getName());
        }
        user.setRoles(rolesStr);

        return user;
    }




    public List<User> getUsersByUserBean(QueryUserBean userBean){

        if (userBean == null) userBean = new QueryUserBean();

        Query<User> query = datastore.find(User.class);
        if (userBean.getGender() != null && !userBean.getGender().equals("请选择") && userBean.getGender().length() != 0)
            query.field("gender").equal(userBean.getGender());

        if (userBean.getEmail() != null)
            query.field("email").containsIgnoreCase(userBean.getEmail());

        if (userBean.getMobile() != null)
            query.field("mobile").containsIgnoreCase(userBean.getMobile());

        if (userBean.getName() != null)
            query.field("name").containsIgnoreCase(userBean.getName());


        if (userBean.getMobile_email_name() != null) {
            String search = userBean.getMobile_email_name();
            query.or(query.criteria("mobile").containsIgnoreCase(search),query.criteria("email").containsIgnoreCase(search),
                    query.criteria("name").containsIgnoreCase(search));
        }

        query.order("-registerDate");
        int size = query.asList().size();
        int allPages = (size + 9)/ userBean.getLimit();
        userBean.setAllPages(allPages);

        query.offset(userBean.getOffset()).limit(userBean.getLimit());



        List<User> users = getUsersWithRoles(query.asList());
        List<User> aUsers = new ArrayList<>();

        if (userBean.getRole() != null && !userBean.getRole().equals("未归类") && userBean.getRole().length() != 0){

            for(int i = 0;i < users.size();i++){
                User user = users.get(i);
                if (user.getRoles() == null || user.getRoles().size() == 0) continue;
                else {
                    for(int j = 0; j < user.getRoles().size();j++){
                        if (userBean.getRole().equals(user.getRoles().get(j))){

                            aUsers.add(user);
                            break;
                        }
                    }
                }

            }

        }else{
            aUsers = users;
        }
        return aUsers;

    }







    public List<User_Role> getUserRoles(ObjectId userId) {
        List<User_Role> userRoles = datastore.createQuery(User_Role.class).field("userId").equal(userId).asList();
        return userRoles;
    }

    public void savePassword(User user, String password) {
        String salt = RandomStringUtils.randomAlphanumeric(4);
        String hashedPassword = DigestUtils.md5Hex(password + salt);
        user.setSalt(salt);
        user.setPassword(hashedPassword);
        datastore.save(user);
    }
    public Boolean verifyPassword(User user, String password) {
        String hashedPassword = DigestUtils.md5Hex(password + user.getSalt());
        if (hashedPassword.equals(user.getPassword())) {
            return true;
        }
        return false;
    }


    /*输入用户列表,为列表内每个用户的roles属性赋值(如果有分类的话)。*/
    private List<User> getUsersWithRoles(List<User> users){

        for (User user: users) {
            List<User_Role> roles = datastore.find(User_Role.class).field("userId").equal(user.getId()).asList();

            List rolesStr = new ArrayList();

            for (int i = 0; i < roles.size(); i++) {
                User_Role user_role = roles.get(i);

                Role role =  datastore.find(Role.class).field("id").equal(user_role.getRoleId()).get();

                rolesStr.add(role.getName());
            }
            user.setRoles(rolesStr);

        }
        return users;
    }

    public Boolean isExistMobile(User postUser){

        User user = datastore.find(User.class).field("mobile").equal(postUser.getMobile()).get();
        return (user != null && !user.getId().equals(postUser.getId())) ;
    }

    public Boolean isExistEmail(User postUser){

        User user = datastore.find(User.class).field("email").equal(postUser.getEmail()).get();
        return (user != null && !user.getId().equals(postUser.getId()));
    }


    /*直接返回数据库所有用户*/
    public List<User> getUsers(){
        List<User> users = datastore.find(User.class).asList();

        return getUsersWithRoles(users);
    }
    /*输入Role 名字,返回所有该角色的用户*/
    public List<User> getUsersByName(String name){

        System.out.println("name is "+name);
        if (name.equals("admin")) name = "管理员";
        else if (name.equals("user")) name = "注册用户";
        else  if(name.equals("data")) name = "数据录入";
        System.out.println("name is "+name);

        Role role = datastore.find(Role.class).field("name").equal(name).get();

        List<User_Role> user_Roles = datastore.find(User_Role.class).field("roleId").equal(role.getId()).asList();


        System.out.println("user_roles size "+ user_Roles.size());

        List<User> users = new ArrayList<>();


        for(int i = 0;i < user_Roles.size();i++){
            User_Role user_role = user_Roles.get(i);
            User user = datastore.find(User.class).field("id").equal(user_role.getUserId()).get();

            users.add(user);
        }

        System.out.println("users size "+ users.size());


        return users;

    }

    public User getUserByMobile(String mobile){

        User user = datastore.find(User.class).field("mobile").equal(mobile).get();
        return user;

    }

//    public void save(User user) {
//
//        List roles = user.getRoles();
//
//        if (user.getUser_role() == null){
//            User_Role user_Role = new User_Role();
//            datastore.save(user_Role);
//            user.setUser_role(user_Role);
//        }
//        datastore.save(user);
//
//        if (roles != null) {
//            //清空
//            user.getUser_role().setRoles(new ArrayList<Role>());
//
//            for (int i = 0; i < roles.size(); i++) {
//                //保证同一个role只有一份(name为key)
//                Role roleQ = datastore.find(Role.class).field("name").equal(roles.get(i)+"").get();
//
//                if (roleQ == null) {
//                    roleQ = new Role();
//                    roleQ.setName("" + roles.get(i));
//                    datastore.save(roleQ);
//                }
//
//                User_Role user_Role = roleQ.getUser_role();
//                if (user_Role == null){
//                    user_Role = new User_Role();
//                    datastore.save(user_Role);
//                    roleQ.setUser_role(user_Role);
//                }
//
//                roleQ.getUser_role().getUsers().add(user);
//                datastore.save(roleQ);
//                user.getUser_role().getRoles().add(roleQ);
//            }
//            datastore.save(user.getUser_role());
//            datastore.save(user);
//        }
//    }






//    public User getUserById(ObjectId id){
//
//        User user = datastore.find(User.class).field("id").equal(id).get();
//
//        List roles = user.getUser_role().getRoles();
//        List rolesStr = new ArrayList();
//
//        for (int i = 0; i < roles.size(); i++) {
//            Role role =(Role) roles.get(i);
//            rolesStr.add(role.getName());
//        }
//        user.setRoles(rolesStr);
//
//        return user;
//    }




//    public List<User> getUsers(){
//
//        List<User> users = datastore.find(User.class).asList();
//
//        for (User user: users) {
//            List roles = user.getUser_role().getRoles();
//            List rolesStr = new ArrayList();
//
//            for (int i = 0; i < roles.size(); i++) {
//                Role role =(Role) roles.get(i);
//                rolesStr.add(role.getName());
//            }
//            user.setRoles(rolesStr);
//
//        }
//
//        return users;
//    }
}
