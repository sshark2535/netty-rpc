package com.song.client.test;


import com.alibaba.fastjson.JSONObject;
import com.song.client.annotation.RemoteInvoke;
import com.song.client.param.Response;
import com.song.client.user.bean.User;
import com.song.client.user.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokeTest.class)
@ComponentScan("\\")
public class RemoteInvokeTest {
    @RemoteInvoke
    public static UserRemote userRemote;

    @Test
    public void testSaveUser(){
        //ClientRequest request = new ClientRequest();

        try{
            User u = new User();
            u.setId(1);
            u.setName("Tom");
            userRemote.saveUser(u);
        }catch (Exception e){
            e.printStackTrace();
        }

//        Response response = userRemote.saveUser(u);
//        System.out.println(JSONObject.toJSONString(response));

//        request.setContext(u);
//        request.setCommand("com.song.user.controller.UserController.saveUser");
//        Response response = TCPClient.send(request);
//        System.out.println("response:"+response.getContext());
    }
}
