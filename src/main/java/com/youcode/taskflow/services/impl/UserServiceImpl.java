package com.youcode.taskflow.services.impl;

import com.youcode.taskflow.dto.UserDto;
import com.youcode.taskflow.entities.User;
import com.youcode.taskflow.repository.UserRepository;
import com.youcode.taskflow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> list = new ArrayList<>();

        userRepository.findAll().forEach(user -> {
            list.add(modelMapper.map(user, UserDto.class));
        });

        return list;
    }

    @Override
    public UserDto addUser(UserDto addUserDto) {

        User user = modelMapper.map(addUserDto,User.class);
        User saved = userRepository.save(user);
        return modelMapper.map(saved,UserDto.class);
    }

    @Override
    public void deleteUser(Integer id) {

        userRepository.deleteById(id);
    }
}
