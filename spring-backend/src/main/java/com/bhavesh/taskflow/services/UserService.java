package com.bhavesh.taskflow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.SignupRequestDTO;
import com.bhavesh.taskflow.models.User;
import com.bhavesh.taskflow.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public boolean createUser(String username, String email, String password) {
        if(!validateUserData(username, email, password)) {
            throw new RuntimeException("Invalid user data");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return true;
    }

    public boolean validateUserData(String username, String email, String password) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Invalid email format");
        }

        if(password == null || password.trim().length() < 8){
            throw new RuntimeException("Password must be at least 8 characters long");
        }

        if (username == null || username.trim().length() < 3) {
            throw new RuntimeException("Username must be at least 3 characters long");
        }

        return true;
    }
    
    public boolean signupUser(SignupRequestDTO signupRequest) {
        return createUser(signupRequest.getUsername(), signupRequest.getEmail(), signupRequest.getPassword());
    }


    public static User getCurrentAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (User) context.getAuthentication().getPrincipal();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
