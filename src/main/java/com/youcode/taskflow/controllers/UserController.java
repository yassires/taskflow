package com.youcode.taskflow.controllers;


import com.youcode.taskflow.dto.UserDto;
import com.youcode.taskflow.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto save(@Valid @RequestBody UserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @GetMapping
    @ResponseBody
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }



    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
    }
}
