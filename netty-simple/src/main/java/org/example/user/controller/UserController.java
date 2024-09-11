package org.example.user.controller;

import com.song.netty.utill.Response;
import com.song.netty.utill.ResponseUtill;
import org.example.user.bean.User;
import org.example.user.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserController {
    @Resource
    private UserService userService;
    public Response saveUser(User user){
        userService.save(user);
        return ResponseUtill.createSuccessResponse(user);
    }
//    public Response saveUsers(List<User> users){
//        userService.saveList(users);
//        return ResponseUtill.createSuccessResponse(users);
//    }
}
