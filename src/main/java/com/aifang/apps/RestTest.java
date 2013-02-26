package com.aifang.apps;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.aifang.apps.bll.ArticleBll;
import com.aifang.apps.common.Params;
import com.aifang.apps.common.TaskList;
import com.aifang.apps.model.ArticleModel;

@Path("/test")
@Component
@Scope("request")
public class RestTest {
    @Resource(name="testTaskList")
    private TaskList taskList;

    @Resource(name="articleBll")
    private ArticleBll articleBll;

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@QueryParam("action") String action){
        Params params=new Params();
        params.set("action", action);
        taskList.run(params);
        return "ok";
    }

    @GET
    @Path("article/{id}")
    @Produces(MediaType.TEXT_XML)
    public  List<ArticleModel> getArticleById(@PathParam("id") int id){
        List<ArticleModel> entities=articleBll.getArticleList();
        articleBll.updateHits(id);
        return entities;
    }

}
