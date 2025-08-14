package com.CasaRoma.FerreteriaApi.controller;

import com.CasaRoma.FerreteriaApi.model.JwtTokenResponse;
import com.CasaRoma.FerreteriaApi.model.User;
import com.CasaRoma.FerreteriaApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> login(@RequestBody User user)
    {
        //System.out.println(user.getUsername() + "LoggedIn");
        JwtTokenResponse response = new JwtTokenResponse(userService.verify(user));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User user)
    {
        userService.register(user);
        return ResponseEntity.noContent().build();
    }
}