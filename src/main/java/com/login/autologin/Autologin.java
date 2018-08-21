package com.login.autologin;

import com.login.model.UserBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class Autologin {
    public void setSecuritySocialContext(UserBean userForm) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userForm.getProvider().toUpperCase()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userForm.getUserId(), userForm.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setSecurityContext(UserBean userForm) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userForm.getProvider().toUpperCase()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userForm.getEmail(), userForm.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
