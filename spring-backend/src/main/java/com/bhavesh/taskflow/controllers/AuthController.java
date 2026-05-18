package com.bhavesh.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.LoginRequest;
import com.bhavesh.taskflow.dtos.SignupRequest;
import com.bhavesh.taskflow.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            return "login-success";
        } catch (Exception e) {
            return "login-failed";
        }
    }
}
