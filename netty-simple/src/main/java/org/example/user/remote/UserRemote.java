package org.example.user.remote;

import com.song.netty.utill.Response;
import org.example.user.bean.User;

import java.util.List;

public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> users);
}
