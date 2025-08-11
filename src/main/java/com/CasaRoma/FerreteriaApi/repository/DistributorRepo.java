package com.CasaRoma.FerreteriaApi.repository;

import com.CasaRoma.FerreteriaApi.model.Distributor;
import com.CasaRoma.FerreteriaApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributorRepo extends JpaRepository<Distributor, Integer> {
}
