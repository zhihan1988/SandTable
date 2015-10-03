package com.rathink.ie.system.organization.dao.hibernate;


import com.rathink.ie.system.organization.dao.UserDao;

import com.ming800.core.base.dao.hibernate.BaseDaoSupport;
import com.rathink.ie.user.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

@Repository
public class UserDaoHibernate extends BaseDaoSupport<User> implements UserDao {


    @Override
    public User getUniqueMyUserByConditions(String branchName, String queryHql, LinkedHashMap<String, Object> queryParamMap) {

        /*if (branchName.contains("_")) {
            SpObserver.putSp(branchName.split("_")[0]);
        } else {
            SpObserver.putSp("dataSource");
        }*/

        Session tempSession = super.getSessionFactory().openSession();

        Query listQuery = tempSession.createQuery(queryHql);
        setQueryParams(listQuery, queryParamMap);
        User myUser = (User) listQuery.uniqueResult();
        tempSession.close();

        return myUser; //To change body of implemented methods use File | Settings | File Templates.
    }

}
