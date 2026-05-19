package com.bhavesh.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.DashboardResponseDTO;
import com.bhavesh.taskflow.services.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getCurrentUserData(){
        return ResponseEntity.ok(dashboardService.getCurrentUserData());
    }
    
}
