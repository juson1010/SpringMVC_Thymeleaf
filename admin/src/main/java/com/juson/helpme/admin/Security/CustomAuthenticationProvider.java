package com.juson.helpme.admin.Security;



import com.juson.helpme.dao.User;
import com.juson.helpme.services.UserService;
import com.juson.helpme.utils.Utils;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by qianliang on 26/6/2016.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private Datastore datastore;

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user;
        if (Utils.isValidMobile(account)) {
            user = datastore.find(User.class).field("mobile").equalIgnoreCase(account).get();

            if (user == null) {
                throw new BadCredentialsException("此手机号码还没有注册,请先注册");
            }
        } else if (Utils.isValidEmail(account)) {
            user = datastore.find(User.class).field("email").equalIgnoreCase(account).get();

            if (user == null) {
                throw new BadCredentialsException("此邮箱还没有注册,请先注册");
            }
        } else {
            throw new BadCredentialsException("无效的手机号码或邮箱");
        }

        if (user.getLocked() != null && user.getLocked()) {
            throw new BadCredentialsException("此账号已被锁定.");
        }

        if (user.getPassword() == null || user.getPassword().length() == 0) {
            throw new BadCredentialsException("您还没有设置密码");
        }

        if (!userService.verifyPassword(user, password)) {
            throw new BadCredentialsException("密码错误");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getId().toHexString());

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    public boolean supports(Class<?> arg0) {
        return true;
    }

}