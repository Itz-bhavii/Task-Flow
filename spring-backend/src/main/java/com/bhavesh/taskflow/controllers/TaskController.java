package com.bhavesh.taskflow.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @GetMapping("/test")
    public String testAuth() {
        return "Authenticated access successful!";
    }
}
