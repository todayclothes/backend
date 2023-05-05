package com.seungah.todayclothes.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("health check success!");
    }
}