package com.CasaRoma.FerreteriaApi.controller;

import com.CasaRoma.FerreteriaApi.model.Distributor;
import com.CasaRoma.FerreteriaApi.model.Product;
import com.CasaRoma.FerreteriaApi.model.ProductDTO;
import com.CasaRoma.FerreteriaApi.service.DistributorService;
import com.CasaRoma.FerreteriaApi.service.ExcelService;
import com.CasaRoma.FerreteriaApi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private ProductService productService;

    @Autowired
    private DistributorService distributorService;

    @Autowired
    private ExcelService excelService;


    @GetMapping("/productos")
    public ResponseEntity<List<ProductDTO>> getProducts()
    {
        List<ProductDTO> body = productService.getProducts();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id)
    {
        ProductDTO body = productService.getProductByID(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/getProductosByQuery")
    public ResponseEntity<Page<ProductDTO>> search(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nombre") String ordenarPor
    ) {
        return ResponseEntity.ok(productService.getByQuery(query, pagina, size, ordenarPor));
    }


    @PostMapping("/productos")
    public ResponseEntity<Product> addProduct(@RequestBody Product productDTO)
    {
        Product created = productService.addProduct(productDTO);
        URI location = URI.create("/productos/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/productos/sendAll")
    public ResponseEntity<String> addProducts(@RequestBody List<Product> productDTOList)
    {
        productService.addProducts(productDTOList);
        return ResponseEntity.ok("Productos cargados con exito");
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id)
    {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private void deleteByDistributorID(@PathVariable int distId)
    {
        productService.deleteByDistributorID(distId);
    }

    @PutMapping("/productos")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product)
    {
        Product body = productService.updateProduct(product);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/distribuidores")
    public ResponseEntity<List<Distributor>> getAllDistributors()
    {
        List<Distributor> distributors = distributorService.getAllDistributors();
        return ResponseEntity.ok(distributors);
    }

    @GetMapping("/distribuidores/{id}")
    public ResponseEntity<Distributor> getDistributorById(@PathVariable int id)
    {
        Distributor distributor = distributorService.getDistributor(id);
        return ResponseEntity.ok(distributor);
    }

    @PostMapping("/distribuidores")
    public ResponseEntity<Distributor> createDistributor(@RequestBody Distributor distributorDTO)
    {
        Distributor distributor = distributorService.createDistributor(distributorDTO);
        URI location = URI.create("/distribuidores/" + distributor.getId());
        return ResponseEntity.created(location).body(distributor);
    }

    @PutMapping("/distribuidores")
    public ResponseEntity<Distributor> updateDistributor(@RequestBody Distributor distributor)
    {
        Distributor body = distributorService.updateDistributor(distributor);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/distribuidores/{id}")
    public ResponseEntity<Void> deleteDistributor(@PathVariable int id)
    {
        distributorService.deleteDistributor(id);
        return ResponseEntity.noContent().build();
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

    @RequestMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
