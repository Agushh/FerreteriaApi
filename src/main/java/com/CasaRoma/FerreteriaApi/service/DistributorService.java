package com.CasaRoma.FerreteriaApi.service;

import com.CasaRoma.FerreteriaApi.exception.ResourceNotFoundException;
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

    public Distributor getDistributor(int id) {
        return distributorRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Distribuidor ID{" + id + "} No Encontrado"));
    }

    public Distributor updateDistributor(Distributor distributor) {
        distributorRepo.findById(distributor.getId()).orElseThrow(() -> new ResourceNotFoundException(distributor + " No Encontrado."));
        return distributorRepo.save(distributor);
    }

    public Distributor createDistributor(Distributor distributor) {
        return distributorRepo.save(distributor);
    }

    public void deleteDistributor(int id) {
        distributorRepo.deleteById(id);
    }

}
