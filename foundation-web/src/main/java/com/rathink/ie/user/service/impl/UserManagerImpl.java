package com.rathink.ie.user.service.impl;


import com.ming800.core.util.StringUtil;
import com.rathink.ie.user.dao.UserDao;
import com.rathink.ie.user.model.User;
import com.rathink.ie.user.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;


/**
 * Created by IntelliJ IDEA.
 * User: ming
 * Date: 12-10-15
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */

@Service
@Transactional
public class UserManagerImpl implements UserDetailsService, UserManager {
    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = null;
        try {
            String queryStr = "FROM User u WHERE u.username=:username";
            LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
            queryParamMap.put("username", username);
            user = userDao.getUniqueMyUserByConditions(username, queryStr, queryParamMap);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error in retrieving user");
        }
        return user;
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.encodePassword("123456", "SHA"));
    }
}
