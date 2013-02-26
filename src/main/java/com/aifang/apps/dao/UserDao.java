package com.aifang.apps.dao;

import com.aifang.apps.model.UserModel;

public class UserDao extends HibernateBaseDao<UserModel> {
    public UserDao(){
        super(UserModel.class);
    }

    /**
     * 通过id获取单个用户信息
     * @param id
     * @return
     */
    public UserModel getUser(int id){
         return findById(id);
    }
}
