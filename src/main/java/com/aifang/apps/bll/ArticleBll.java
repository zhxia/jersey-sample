package com.aifang.apps.bll;

import java.io.Serializable;
import java.util.List;

import com.aifang.apps.dao.ArticleDao;
import com.aifang.apps.model.ArticleModel;

public class ArticleBll {
    private ArticleDao articleDao;

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public ArticleModel getArticle(int Id){
        return this.articleDao.findById(Id);
    }

    public List<ArticleModel> getArticleList(){
        return this.articleDao.getArticles();
    }

    public int updateHits(int articleId){
        Serializable[] ids=articleDao.findArticleIds();
        if(null!=ids){
            for(Serializable id:ids){
                System.out.println(id);
            }
        }
        return articleDao.updateHits(articleId);
    }
}
