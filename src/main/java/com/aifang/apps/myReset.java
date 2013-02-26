package com.aifang.apps;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.aifang.apps.bll.ArticleBll;
import com.aifang.apps.bll.UserBll;
import com.aifang.apps.model.UserModel;

@Path("/user")
@Component
@Scope("request")
public class myReset {
    private static int count=0;
    @Resource(name="userBll")
    private UserBll userBll;

    @Resource(name="articleBll")
    private ArticleBll articleBll;


    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<UserModel> getUserList(){
        List<UserModel> list=new ArrayList<UserModel>();
        UserModel u1=new UserModel();
        u1.setId(1);
        u1.setUserName("zhxia");
        list.add(u1);
        UserModel u2=new UserModel();
        u2.setId(2);
        u2.setUserName("zhxia114");
        list.add(u2);
        return list;
    }

    @GET
    @Path("/show/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel getUser(@PathParam("id") int id){
        return userBll.getUser(id);
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addUser(MultivaluedMap<String, String> formParams){
        String username=formParams.getFirst("name").trim();
        String password=formParams.getFirst("password").trim();
        String mobile=formParams.getFirst("mobile").trim();
        userBll.addUser(username, password,mobile);
        return Response.ok(formParams.toString()).build();
    }


    @GET
    @Path("/test")
    @Produces({MediaType.TEXT_PLAIN})
    public String test(){
        return String.valueOf(count++);
    }

}
