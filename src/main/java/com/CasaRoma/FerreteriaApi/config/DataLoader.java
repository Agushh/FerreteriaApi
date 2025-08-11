package com.CasaRoma.FerreteriaApi.config;

import com.CasaRoma.FerreteriaApi.model.User;
import com.CasaRoma.FerreteriaApi.repository.UserRepo;
import com.CasaRoma.FerreteriaApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) {
        userService.registerMain();
    }
}