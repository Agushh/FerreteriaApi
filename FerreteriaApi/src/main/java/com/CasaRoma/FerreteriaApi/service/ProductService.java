package com.CasaRoma.FerreteriaApi.service;

import com.CasaRoma.FerreteriaApi.model.Distributor;
import com.CasaRoma.FerreteriaApi.model.Product;
import com.CasaRoma.FerreteriaApi.model.ProductDTO;
import com.CasaRoma.FerreteriaApi.repository.DistributorRepo;
import com.CasaRoma.FerreteriaApi.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    DistributorRepo distributorRepo;

    public void addProduct(Product product)
    {
        Product pr = validateProduct(product);
        System.out.println(pr.getId());
        pr.setId(null);
        productRepo.save(pr);
    }

    public void addProducts(List<Product> products) {
        productRepo.saveAll(products.stream().map(this::validateProduct).toList());
    }

    public Page<ProductDTO> getByQuery(String query, int pagina, int tamanio, String ordenarPor) {
        Pageable pageable = PageRequest.of(pagina, tamanio, Sort.by(ordenarPor).ascending());
        Specification<Product> spec = ProductSpecification.buscarPorQuery(query);
        return productRepo.findAll(spec, pageable).map(this::productToDto);
    }


    public List<ProductDTO> getProducts()
    {
        return productRepo.findAll().stream().map(this::productToDto).toList();
    }

    public ProductDTO getProductByID(int id)
    {
        return productToDto(productRepo.findById(id).orElse(new Product()));
    }

    public void deleteProduct(int id)
    {
        productRepo.deleteById(id);
    }

    @Transactional
    public void deleteByDistributorID(int distId) {
        productRepo.deleteByDistributor(distributorRepo.findById(distId).orElseThrow(() -> new RuntimeException("Distribuidor no encontrado")));
    }

    public void updateProduct(Product product)
    {
        Product pr = productRepo.findById(product.getId()).orElseThrow(() -> new RuntimeException("No encontrado."));
        productRepo.save(validateProduct(product));
    }

    private ProductDTO productToDto(Product product)
    {
        return new ProductDTO(validateProduct(product));
    }

    private Product validateProduct(Product product)
    {
        product.setId(product.getId() != null ? product.getId() : 0);
        product.setCategoria(product.getCategoria() != null ? product.getCategoria() : "");
        product.setNombre(product.getNombre() != null ? product.getNombre() : "");
        product.setDistributor(product.getDistributor() != null ? product.getDistributor() : distributorRepo.findById(product.getIdOfDistributor()).orElse(null));
        product.setPrecioLista(product.getPrecioLista() != 0 ? product.getPrecioLista() : 0);
        product.setIdFromDistributor(product.getIdFromDistributor() != null ? product.getIdFromDistributor() : "");
        return product;
    }

}
