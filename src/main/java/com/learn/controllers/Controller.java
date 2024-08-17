package com.learn.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller
{

    @GetMapping("/hi")
    public String display()
    {
        return "Hello";
    }


    //if PreAuthorized the method will not run without authentication
    //    @PreAuthorize("/hasRole('USER')")
    // USER role defined in SecurityConfig with each user when created
    //admin will not be able to access it
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hiuser")
    public String userDisplay()
    {
        return "Hello user";
    }

    @GetMapping("/hiadmin")
    public String adminDisplay()
    {
        return "Hello admin";
    }
}
