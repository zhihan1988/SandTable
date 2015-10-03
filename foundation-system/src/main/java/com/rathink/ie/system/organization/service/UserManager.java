package com.rathink.ie.system.organization.service;

import com.ming800.core.taglib.PageEntity;
import com.ming800.core.does.model.PageInfo;
import com.rathink.ie.user.model.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ming
 * Date: 12-10-15
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public interface UserManager {


    public List<User> listBigUser(String teachAreaId);

    public void saveOrUpdateBigUser(User bigUser);

    public PageInfo pageBigUser(String teachAreaId, PageEntity pageEntity);
}
