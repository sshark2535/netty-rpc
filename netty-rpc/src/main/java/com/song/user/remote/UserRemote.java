package com.song.user.remote;

import com.song.netty.utill.Response;
import com.song.user.bean.User;

import java.util.List;

public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> users);
}
