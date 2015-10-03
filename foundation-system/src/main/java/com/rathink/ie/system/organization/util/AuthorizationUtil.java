package com.rathink.ie.system.organization.util;


import com.rathink.ie.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * @author WuYingbo
 */
public class AuthorizationUtil {


    public static User getMyUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        try {
            return (User) authentication.getPrincipal();
        } catch (Exception e) {
            User myUser = new User();
            return myUser;


        }
    }



}
