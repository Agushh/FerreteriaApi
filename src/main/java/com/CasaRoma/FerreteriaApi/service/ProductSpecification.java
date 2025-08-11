package com.CasaRoma.FerreteriaApi.service;

import com.CasaRoma.FerreteriaApi.model.Product;
import com.CasaRoma.FerreteriaApi.model.ProductDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    //todo eliminar lo de distribuidor ID porque nunca se va a usar esa busqueda. Añadir filtros de orden.
    public static Specification<Product> buscarPorQuery(String query) {
        return (root, cq, cb) -> {
            if (query == null || query.trim().isEmpty()) {
                return cb.conjunction(); // No aplicar filtros si la query está vacía
            }

            String[] palabras = query.trim().toLowerCase().split("\\s+");
            List<Predicate> predicadosFinales = new ArrayList<>();

            for (String palabra : palabras) {
                String like = "%" + palabra + "%";

                Predicate enNombre = cb.like(cb.lower(root.get("nombre")), like);
                Predicate enCategoria = cb.like(cb.lower(root.get("categoria")), like);
                Predicate enDistribuidor = cb.like(cb.lower(root.join("distributor").get("name")), like);

                // Si es número, buscar por ID de distribuidor también
                Predicate porIdDistribuidor = palabra.matches("\\d+")
                        ? cb.equal(root.join("distributor").get("id"), Long.parseLong(palabra))
                        : cb.disjunction(); // un "falso" que no afecta si no es número

                // Cualquiera de los campos debe contener la palabra
                Predicate predicadoPorPalabra = cb.or(enNombre, enCategoria, enDistribuidor, porIdDistribuidor);

                // Cada palabra debe cumplir (AND)
                predicadosFinales.add(predicadoPorPalabra);
            }

            return cb.and(predicadosFinales.toArray(new Predicate[0]));
        };
    }

}
