package com.bhavesh.taskflow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.SignupRequest;
import com.bhavesh.taskflow.models.User;
import com.bhavesh.taskflow.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    
    public boolean createUser(String username, String email, String password) {
        validateUserData(username, email, password);

        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);
        return true;
    }

    public boolean validateUserData(String username, String email, String password) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }

        if(password == null || password.trim().length() < 8){
            return false;
        }

        if (username == null || username.trim().length() < 3) {
            return false;
        }

        return true;
    }
    
    public boolean signupUser(SignupRequest signupRequest) {
        return createUser(signupRequest.getUsername(), signupRequest.getEmail(), signupRequest.getPassword());
    }
}
