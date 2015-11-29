package com.rathink.ie.user.util;


import com.rathink.ie.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthorizationUtil {

    public static User getMyUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        try {

            return (User) authentication.getPrincipal();
        } catch (Exception e){
            return new User();
        }
    }

}
