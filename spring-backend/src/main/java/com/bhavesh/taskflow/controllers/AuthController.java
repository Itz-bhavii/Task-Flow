package com.bhavesh.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bhavesh.taskflow.dtos.LoginRequest;
import com.bhavesh.taskflow.dtos.SignupRequest;
import com.bhavesh.taskflow.services.UserService;

@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signupRequest) {
        if(userService.signupUser(signupRequest)) {
            return "signup-success";
        } else {
            return "signup-failed";
        }
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // have to validate jwt token and check weather it have access or not
        return "login-success";
    }
}
