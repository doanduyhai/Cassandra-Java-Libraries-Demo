package com.datastax.demo;

import com.datastax.driver.mapping.Mapper;

public class UserService {

    Mapper<UserEntity> mapper;

    public void create(String login, String name, int age){

    }

    public UserEntity find(String login) {
        return null;
    }

    public void delete(String login) {

    }
}
