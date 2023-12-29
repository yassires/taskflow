package com.youcode.taskflow.services;

import com.youcode.taskflow.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public List<UserDto> getAllUsers();

    UserDto addUser(UserDto addUserDto);

    void deleteUser(Integer id);
}
