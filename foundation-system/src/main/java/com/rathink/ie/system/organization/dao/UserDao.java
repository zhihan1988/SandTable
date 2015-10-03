package com.rathink.ie.system.organization.dao;

import com.ming800.core.base.dao.BaseDao;
import com.rathink.ie.user.model.User;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ming
 * Date: 12-10-16
 * Time: 下午4:18
 * To change this template use File | Settings | File Templates.
 */
public interface UserDao extends BaseDao<User> {


    public User getUniqueMyUserByConditions(String branchName, String queryHql, LinkedHashMap<String, Object> queryParamMap);

}
