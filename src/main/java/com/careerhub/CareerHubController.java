package com.careerhub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CareerHubController {
    @Autowired
    private CareerHubUserService userService;

    @GetMapping(value = "/getUserById")
    public Optional<CareerHubUser> getUserById(String id) {
        return userService.getUserById(id);
    }
}