package com.aifang.apps.dao;

import java.io.Serializable;
import java.util.List;

import com.aifang.apps.model.ArticleModel;

public class ArticleDao extends HibernateBaseDao<ArticleModel> {
    public ArticleDao(){
        super(ArticleModel.class);
    }

    public List<ArticleModel> getArticles(){
        StringBuilder sb=new StringBuilder("select * from ");
        sb.append(getTableName());
        return executeQuery(sb.toString());
    }

    public int updateHits(int articleId){
        StringBuilder sqlBuilder=new StringBuilder("update ");
        sqlBuilder.append(getTableName()).append(" set hits=hits+1 where id=?");
        return executeUpdate(sqlBuilder.toString(), new Integer[]{articleId});
    }

    public Serializable[] findArticleIds(){
        return findIds(null, null, 0, 100);
    }
}
