package com.CasaRoma.FerreteriaApi.controller;

import com.CasaRoma.FerreteriaApi.model.Distributor;
import com.CasaRoma.FerreteriaApi.model.Product;
import com.CasaRoma.FerreteriaApi.model.ProductDTO;
import com.CasaRoma.FerreteriaApi.service.DistributorService;
import com.CasaRoma.FerreteriaApi.service.ExcelService;
import com.CasaRoma.FerreteriaApi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    ProductService productService;

    @Autowired
    DistributorService distributorService;

    @Autowired
    ExcelService excelService;


    @GetMapping("/productos")
    public List<ProductDTO> getProducts()
    {
        return productService.getProducts();
    }

    @GetMapping("/productos/{id}")
    public ProductDTO getProductById(@PathVariable int id)
    {
        return productService.getProductByID(id);
    }

    @GetMapping("/getProductosByQuery")
    public Page<ProductDTO> search(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nombre") String ordenarPor
    ) {
        return productService.getByQuery(query, pagina, size, ordenarPor);
    }

    @PostMapping("/productos")
    public void addProduct(@RequestBody Product productDTO)
    {
        productService.addProduct(productDTO);
    }

    @PostMapping("/sendProductos")
    public void addProducts(@RequestBody List<Product> productDTOList)
    {
        productService.addProducts(productDTOList);
    }

    @DeleteMapping("/productos/{id}")
    public void deleteProduct(@PathVariable int id)
    {
        productService.deleteProduct(id);
    }
    @DeleteMapping("/productos/deleteByDist/{distId}")
    public void deleteByDistributorID(@PathVariable int distId)
    {
        productService.deleteByDistributorID(distId);
    }

    @PutMapping("/productos")
    public void updateProduct(@RequestBody Product product)
    {
        productService.updateProduct(product);
    }

    @GetMapping("/distribuidores")
    public List<Distributor> getAllDistributors()
    {
        return distributorService.getAllDistributors();
    }

    @GetMapping("/distribuidores/{id}")
    public Distributor getDistributorById(@PathVariable int id)
    {
        return distributorService.getDistributor(id);
    }

    @PostMapping("/distribuidores")
    public void createDistributor(@RequestBody Distributor distributorDTO)
    {
        distributorService.createDistributor(distributorDTO);
    }

    @PutMapping("/distribuidores")
    public void updateDistributor(@RequestBody Distributor distributor)
    {
        distributorService.updateDistributor(distributor);
    }

    @DeleteMapping("/distribuidores/{id}")
    public void deleteDistributor(@PathVariable int id)
    {
        distributorService.deleteDistributor(id);
    }

    @PostMapping("/excel/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file,
                                         @RequestParam("distribuidorId") Integer distribuidorId) {

        if (file.isEmpty() || distribuidorId == null) {
            return ResponseEntity.badRequest().body("Archivo o distribuidor inválido");
        }

        deleteByDistributorID(distribuidorId);

        String retorno = excelService.deserialize(file, distribuidorId);


        return ResponseEntity.ok(retorno);
    }
}
