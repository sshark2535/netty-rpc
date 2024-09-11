package com.song.client.user.remote;



import com.song.client.param.Response;
import com.song.client.user.bean.User;

import java.util.List;

public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> users);
}
