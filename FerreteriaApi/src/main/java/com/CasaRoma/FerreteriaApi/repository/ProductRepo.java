package com.CasaRoma.FerreteriaApi.repository;

import com.CasaRoma.FerreteriaApi.model.Distributor;
import com.CasaRoma.FerreteriaApi.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepo extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.distributor = :distributor")
    public void deleteByDistributor(@Param("distributor") Distributor distributor);
}
