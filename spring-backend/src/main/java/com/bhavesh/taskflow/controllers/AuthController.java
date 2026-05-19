package com.bhavesh.taskflow.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.LoginRequestDTO;
import com.bhavesh.taskflow.dtos.SignupRequestDTO;
import com.bhavesh.taskflow.security.JwtUtility;
import com.bhavesh.taskflow.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup( @RequestBody SignupRequestDTO signupRequest) {

        boolean created = userService.signupUser(signupRequest);
        if (created) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message","User created successfully"));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message","Signup failed"));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            String token = jwtService.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok(Map.of("token",token));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message","Invalid email or password"));
        }
    }

    
}
