package com.aifang.apps.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public interface IBaseDao<T> {
    Serializable insert(T entity);
    void delete(Serializable Id);
    int delete(Serializable...Ids);
    int deleteAll();
    int update(String queryString,Object[] values);
    int update(String queryString,Object value);
    int update(String queryString);
    void update(T entity);
    void saveOrUpdate(T entity);
    List<T> findAll();
    List<T> find(Criterion where,Order[] orders,int offset,int limit);
    List<T> findByIds(Object...ids);
    T findById(Serializable Id);
    Map<Serializable, T> findByIdsMap(Object...ids);
    T findRow(Criterion where,Order[] orders);
    long findCount(Criterion where);
    List<T> executeQuery(String sql);
    int executeUpdate(String sql);
    Serializable[] findIds(Criterion where,Order[] orders,int offset,int limit);
}
