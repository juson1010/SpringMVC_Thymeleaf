package com.juson.helpme.admin.Security;



import com.juson.helpme.dao.User;
import com.juson.helpme.dao.User_Role;
import com.juson.helpme.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qianliang on 26/6/2016.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserById(new ObjectId(username));
        List<User_Role> userRoles = userService.getUserRoles(new ObjectId(username));

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with userId '%s'.", username));
        } else {
            return new CustomUserDetails(user, userRoles);
        }
    }
}
