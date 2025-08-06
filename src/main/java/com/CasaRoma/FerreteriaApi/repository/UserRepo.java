package com.CasaRoma.FerreteriaApi.repository;

import com.CasaRoma.FerreteriaApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
}
