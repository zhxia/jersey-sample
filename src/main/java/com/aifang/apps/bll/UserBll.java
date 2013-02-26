package com.aifang.apps.bll;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.aifang.apps.dao.UserDao;
import com.aifang.apps.model.UserModel;

public class UserBll {
    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public long getCount(){
        return this.userDao.findCount(null);
    }

    public List<UserModel> getUserList(){
        return this.userDao.findAll();
    }

    public UserModel getOne(){
        Criterion where=Restrictions.ne("userName", "null");
        Order[] orders=new Order[]{Order.asc("Id")};
        return this.userDao.findRow(where, orders);
    }

    public UserModel getUser(int userId){
        return this.userDao.getUser(userId);
    }

    public int deleteByIds(){
        return this.userDao.delete(1,2,4);
    }

    public int addUser(String username,String password,String mobile){
        UserModel user=new UserModel();
        user.setUserName(username);
        user.setPassword(password);
        user.setMobile(mobile);
        return (Integer)userDao.insert(user);
    }
}
