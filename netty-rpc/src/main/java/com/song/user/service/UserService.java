package com.song.user.service;

import com.song.user.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    public User save(User user) {
        Integer id = user.getId();
        String name = user.getName();
        System.out.println("id:"+id+"name:"+name);
        return user;
    }

    public void saveList(List<User> users) {

    }
}
