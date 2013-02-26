package com.aifang.apps.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public abstract class HibernateBaseDao<T> implements IBaseDao<T> {
    private Class<T> clazz;
    public HibernateBaseDao(Class<T> entityClass){
        clazz=entityClass;
    }

    private HibernateTemplate hibernateTemplate;
    public void setSessionFactory(SessionFactory sessionFactory){
        this.hibernateTemplate=new HibernateTemplate(sessionFactory);
        this.hibernateTemplate.setCacheQueries(true);
    }


    /**
     *
     */
    @SuppressWarnings("unchecked")
    public List<T> executeQuery(final String sql) {
        return (List<T>)hibernateTemplate.executeWithNativeSession(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                    return session.createSQLQuery(sql).addEntity(clazz).list();
            }
        });
    }

    /**
     * 通过SQL语句进行更新，不需参数绑定
     * @param sql
     */
    public int executeUpdate(String sql) {
        return this.executeUpdate(sql, null);
    }

    /**
     * 通过SQL语句绑定参数进行更新
     * @param sql
     * @param sqlParams
     * @return
     */
    public int executeUpdate(final String sql,final Serializable[] sqlParams){
        return (Integer)hibernateTemplate.executeWithNativeSession(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                SQLQuery sqlQuery=session.createSQLQuery(sql);
                if(null!=sqlParams){
                    for(int i=0;i<sqlParams.length;i++){
                        sqlQuery.setParameter(i, sqlParams[i]);
                    }
                }
                return sqlQuery.executeUpdate();
            }
        });
    }




    /**
     * 通过主键进行查询
     */
    @SuppressWarnings("unchecked")
    public T findById(Serializable Id) {
        return (T)hibernateTemplate.get(clazz, Id);
    }

    /**
     * 单条记录插入
     */
    public Serializable insert(final T entity) {
        return hibernateTemplate.save(entity);
    }

    /**
     * 根据ID进行删除
     */
    @SuppressWarnings("unchecked")
    public void delete(Serializable Id) {
        T entity=(T)hibernateTemplate.get(clazz, Id);
        hibernateTemplate.delete(entity);
    }

    /**
     * 根据批量id进行删除
     */
    public int delete(final Serializable...Ids) {
        String entityName=getEntityName(clazz);
        String entityPrimaryName=getPrimaryPropertyName(clazz);
        final String queryString=String.format("delete %s where %s in(:ids)",entityName,entityPrimaryName);
        Integer updateCount=(Integer)hibernateTemplate.executeWithNativeSession(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                Query queryObject=session.createQuery(queryString);
                if(null!=Ids){
                    queryObject.setParameterList("ids", Ids);
                }
                return new Integer(queryObject.executeUpdate());
            }
        });
        return updateCount.intValue();
    }

    /**
     * 删除所有记录
     */
    public int deleteAll() {
        Integer updateCount=(Integer)hibernateTemplate.executeWithNativeSession(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                String entityName=getEntityName(clazz);
                String queryString=String.format("delete %s",entityName);
                Query queryObject=session.createQuery(queryString);
                return (Integer)queryObject.executeUpdate();
            }
        });
        return updateCount.intValue();
    }

    /**
     * 更新单条记录
     */
    public void update(T entity) {
        hibernateTemplate.update(entity);
    }

    /**
     * 根据hql语句进行更新,传递多个参数值
     */
    public int update(String queryString,Object[] values) {
        return hibernateTemplate.bulkUpdate(queryString,values);
    }

    /**
     * 根据hql语句进行更新，传递单个参数值
     */
    public int update(String queryString, Object value) {
        return hibernateTemplate.bulkUpdate(queryString, value);
    }


    public int update(String queryString) {
        return hibernateTemplate.bulkUpdate(queryString);
    }

    /**
     * 插入或更新记录
     */
    public void saveOrUpdate(T entity) {
        hibernateTemplate.saveOrUpdate(entity);
    }

    /**
     * 读取所有记录
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return (List<T>)hibernateTemplate.loadAll(clazz);
    }

    /**
     * 根据条件读取记录
     */
    @SuppressWarnings("unchecked")
    public List<T> find(final Criterion where, final Order[] orders,final int offset, final int limit) {
        return (List<T>)hibernateTemplate.executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                Criteria criteria=session.createCriteria(clazz);
                criteria.add(where);
                for(Order order:orders){
                    criteria.addOrder(order);
                }
                return (List<T>)criteria.setFirstResult(offset).setMaxResults(limit).list();
            }
        });
    }

    /**
     * 通过id批量获取记录
     */
    @SuppressWarnings("unchecked")
    public List<T> findByIds(final Object... ids) {
        return (List<T>)hibernateTemplate.executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                Criteria criteria=session.createCriteria(clazz);
                String pk=getPrimaryPropertyName(clazz);
                Criterion criterion=Restrictions.in(pk, ids);
                return (List<T>)criteria.add(criterion).list();
            }
        });
    }

    /**
     * 通过id批量获取记录并保持id关联
     */
    public Map<Serializable, T> findByIdsMap(Object... ids) {
        Map<Serializable, T> map=new HashMap<Serializable, T>();
        List<T> list=findByIds(ids);
        if(null!=list){
            for(T t:list){
                Serializable identifierPropertyValue=getPrimaryPropertyValue(t,EntityMode.POJO);
                map.put(identifierPropertyValue, t);
            }
        }
        return map;
    }

    /**
     * 根据条件读取单条记录
     */
    public T findRow(Criterion where, Order[] orders) {
        List<T> list=find(where, orders,0,1);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 根据条件读取记录数
     */
    public long findCount(final Criterion where) {
        long rowCount=(Long)hibernateTemplate.execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                Criteria criteria=session.createCriteria(clazz);
                if(null!=where){
                    criteria.add(where);
                }
                return (Long)criteria.setProjection(Projections.rowCount()).uniqueResult();
            }
        });
        return rowCount;
    }

    @SuppressWarnings("unchecked")
    public Serializable[] findIds(final Criterion where,final Order[] orders,final int offset,
           final int limit) {
        return (Serializable[])hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException,
                    SQLException {
                Criteria criteria=session.createCriteria(clazz);
                if(null!=where){
                    criteria.add(where);
                }
                if(null!=orders){
                   for(Order order:orders){
                       criteria.addOrder(order);
                   }
                }
                List<T> list=(List<T>)criteria.setFirstResult(offset).setMaxResults(limit).list();
                List<Serializable> pkIds=null;
                if(null!=list){
                    pkIds=new ArrayList<Serializable>();
                    for(T t:list){
                       Serializable pkValue=getPrimaryPropertyValue(t,EntityMode.POJO);
                       pkIds.add(pkValue);
                    }
                }
                return pkIds.toArray(new Serializable[]{});
            }
        });
    }


    //==============================================内部方法=========================================================



    /**
     * 获取表名
     */
    protected String getTableName(){
        AbstractEntityPersister classMetadata=(SingleTableEntityPersister)hibernateTemplate.getSessionFactory().getClassMetadata(clazz);
        return classMetadata.getTableName();
    }
    /**
     * 获取Pojo的Id属性
     * @param entityClass
     * @return
     */
    protected String getPrimaryPropertyName(Class<T> entityClass){
        AbstractEntityPersister classMetadata=(SingleTableEntityPersister)hibernateTemplate.getSessionFactory().getClassMetadata(entityClass);
        return classMetadata.getIdentifierPropertyName();
    }

    /**
     * 获取Pojo的Id值
     * @param entity
     * @param propertyName
     * @param entityMode
     * @return
     */
    protected Serializable getPrimaryPropertyValue(T entity,EntityMode entityMode){
        AbstractEntityPersister classMetadata=(SingleTableEntityPersister)hibernateTemplate.getSessionFactory().getClassMetadata(entity.getClass());
        return classMetadata.getIdentifier(entity, entityMode);
    }

    /**
     * 获取Pojo的名称
     * @param entityClass
     * @return
     */
    protected String getEntityName(Class<T> entityClass){
        AbstractEntityPersister classMetadata=(SingleTableEntityPersister)hibernateTemplate.getSessionFactory().getClassMetadata(entityClass);
        return classMetadata.getEntityName();
    }

}
