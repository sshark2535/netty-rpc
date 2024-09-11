package com.song.user.remote;

import com.song.netty.annotation.Remote;
import com.song.netty.utill.Response;
import com.song.netty.utill.ResponseUtill;
import com.song.user.bean.User;
import com.song.user.service.UserService;

import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote{
    private UserService userService;
    public Response saveUser(User user){
        userService.save(user);
        return ResponseUtill.createSuccessResponse(user);
    }
    public Response saveUsers(List<User> users){
        userService.saveList(users);
        return ResponseUtill.createSuccessResponse(users);
    }
}
