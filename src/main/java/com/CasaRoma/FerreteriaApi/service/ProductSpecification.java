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

            // Aquí agregamos el ordenamiento

            // Vamos a priorizar solo el campo "nombre" para empezar con la palabra
            // Solo con la primer palabra de la query para simplificar (o iterar si querés)

            String primeraPalabra = palabras[0];

            // CASE WHEN lower(nombre) LIKE 'primeraPalabra%' THEN 0 ELSE 1 END
            var caseOrder = cb.selectCase()
                    .when(cb.like(cb.lower(root.get("nombre")), primeraPalabra + "%"), 0)
                    .otherwise(1);

            // Aplicar el order by al CriteriaQuery
            assert cq != null;
            cq.orderBy(cb.asc(caseOrder), cb.asc(root.get("nombre")));

            return cb.and(predicadosFinales.toArray(new Predicate[0]));
        };
    }

}
