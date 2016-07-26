package com.juson.springMVC.Controller;




import com.juson.helpme.dao.User;
import com.juson.helpme.services.UserService;
import com.juson.helpme.utils.Utils;
import com.juson.springMVC.Security.JwtService;
import com.juson.springMVC.dto.user.AccountPassword;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qianliang on 25/6/2016.
 */
@Controller
@RequestMapping(value = "login")
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Datastore datastore;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "password", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AccountPassword accountPassword, HttpServletRequest request) throws AuthenticationException {

        System.out.println("login/password");

        User user;

        if (Utils.isValidMobile(accountPassword.getAccount())) {
            user = datastore.find(User.class).field("mobile").equalIgnoreCase(accountPassword.getAccount()).get();

            if (user == null) {
                throw new BadCredentialsException("此手机号码还没有注册,请先注册");
            }
        } else if (Utils.isValidEmail(accountPassword.getAccount())) {
            user = datastore.find(User.class).field("email").equalIgnoreCase(accountPassword.getAccount()).get();

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

        if (!userService.verifyPassword(user, accountPassword.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // Perform the security
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getId().toHexString());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final String token = jwtService.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(token);
    }
}
