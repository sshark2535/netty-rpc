package org.example.test;

import com.alibaba.fastjson.JSONObject;

import org.example.client.TCPClient;
import org.example.user.bean.User;
import org.example.utill.Request;

import org.example.utill.Response;
import org.junit.jupiter.api.Test;

public class TCPTest {
    @Test
    public void testGetResponse(String[] args) {
        Request request = new Request();
        request.setContext("hello, it's tcp test client!!");
        Response response = JSONObject.parseObject(TCPClient.send(request).toString(), Response.class);
        System.out.println("test client received from server: "+response.getContext());
    }
    @Test
    public void testSaveUser(){
        Request request = new Request();
        User u = new User();
        u.setId(1);
        u.setName("Tom");
        request.setContext(u);
        request.setCommand("com.song.user.controller.UserController.saveUser");
        Response response = TCPClient.send(request);
        System.out.println("response:"+response.getContext());
    }
}
