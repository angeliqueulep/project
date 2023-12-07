package com.humber.project.service;

import com.humber.project.model.Users;
import com.humber.project.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users getUserByUsername(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()){
            return null;
        }

        Users users = usersRepository.findByUsername(username);
        if (users != null && users.getPassword().equals(password)) {
            return users;
        }
        return null;
    }
}