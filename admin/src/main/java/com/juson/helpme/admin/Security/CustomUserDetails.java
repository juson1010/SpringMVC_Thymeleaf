package com.juson.helpme.admin.Security;


import com.juson.helpme.dao.User;
import com.juson.helpme.dao.User_Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by qianliang on 26/6/2016.
 */
public class CustomUserDetails implements UserDetails {

    private User user;
    private List<User_Role> userRoles;
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public CustomUserDetails(){

        System.out.println("run custome ");
    }
    public CustomUserDetails(User user, List<User_Role> userRoles) {
        this.user = user;
        this.userRoles = userRoles;
        for (User_Role userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRoleId()));
        }
    }

    public User getUser() {
        return user;
    }

    public List<User_Role> getUserRoles() {
        return userRoles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {   return user.getName();}

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getLocked() == null || !user.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
