package com.CasaRoma.FerreteriaApi.controller;

import com.CasaRoma.FerreteriaApi.model.User;
import com.CasaRoma.FerreteriaApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody User user)
    {
        System.out.println(user.getUsername());
        return userService.verify(user);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user)
    {
        return userService.register(user);
    }




}
