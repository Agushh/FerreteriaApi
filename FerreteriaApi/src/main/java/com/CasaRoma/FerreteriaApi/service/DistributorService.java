package com.CasaRoma.FerreteriaApi.service;

import com.CasaRoma.FerreteriaApi.model.Distributor;
import com.CasaRoma.FerreteriaApi.repository.DistributorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributorService {

    @Autowired
    DistributorRepo distributorRepo;


    public List<Distributor> getAllDistributors()
    {
        return distributorRepo.findAll();
    }

    public Distributor getDistributor(int id)
    {
        return distributorRepo.findById(id).orElse(new Distributor());
    }

    public void updateDistributor(Distributor distributor)
    {
        Distributor dis = distributorRepo.findById(distributor.getId()).orElseThrow(() -> new RuntimeException("No encontrado"));
        distributorRepo.save(distributor);
    }

    public void createDistributor(Distributor distributor)
    {
        distributorRepo.save(distributor);
    }

    public void deleteDistributor(int id)
    {
        distributorRepo.deleteById(id);
    }

}
