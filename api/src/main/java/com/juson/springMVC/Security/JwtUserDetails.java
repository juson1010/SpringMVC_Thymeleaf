package com.juson.springMVC.Security;


import com.juson.helpme.dao.User;
import com.juson.helpme.dao.User_Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by qianliang on 25/6/2016.
 */
public class JwtUserDetails implements UserDetails {

    private User user;
    private List<User_Role> user_roles;
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public JwtUserDetails(User user, List<User_Role> user_roles) {
        this.user = user;
        this.user_roles = user_roles;
        for (User_Role user_Role : user_roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user_Role.getRoleId()));
        }
    }

    public User getUser() {
        return user;
    }

    public List<User_Role> getUser_roles() {
        return user_roles;
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
    public String getUsername() {
        return user.getId().toHexString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getLocked();
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
