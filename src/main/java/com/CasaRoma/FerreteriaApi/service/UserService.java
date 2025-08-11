package com.CasaRoma.FerreteriaApi.service;

import com.CasaRoma.FerreteriaApi.model.User;
import com.CasaRoma.FerreteriaApi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(User user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public String verify(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return jwtService.generateToken(user.getUsername());
    }

    public void registerMain() {
        if (userRepo.count() == 0) register(new User(0, "admin", "admin123_password"));
    }
}
