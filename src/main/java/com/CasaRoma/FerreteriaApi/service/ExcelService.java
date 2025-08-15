package com.CasaRoma.FerreteriaApi.service;

import com.CasaRoma.FerreteriaApi.exception.ResourceNotFoundException;
import com.CasaRoma.FerreteriaApi.model.Distributor;
import com.CasaRoma.FerreteriaApi.model.Product;
import com.CasaRoma.FerreteriaApi.repository.DistributorRepo;
import com.CasaRoma.FerreteriaApi.repository.ProductRepo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class ExcelService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private DistributorRepo distributorRepo;

    public String deserialize(MultipartFile file, Integer distributorID) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            String ConstantCategory = "";
            String SecondCategory = "";
            int groupindex = 0;
            Product localProduct = new Product();
            localProduct.setDistributor(distributorRepo.findById(distributorID).orElseThrow(() -> new ResourceNotFoundException("Distribuidor no encontrado.")));
            List<Product> productsBuffer = new ArrayList<>();

            for (Row row : sheet) {
                try {

                    if (row.getRowNum() == 0) continue;

                    switch (localProduct.getDistributor().getName().toLowerCase()) {
                        case "sg electricidad":

                            break;

                        case "poxipol":
                            if (row.getCell(1) == null || row.getCell(3) == null) continue; //categoria puede ser nula
                            localProduct.setNombre(getSafeStringCell(row, 1));
                            localProduct.setCategoria(getSafeStringCell(row, 2));
                            localProduct.setPrecioLista(getSafeNumericCell(row, 3));
                            break;

                        case "nexo":
                            if(row.getCell(1) != null) ConstantCategory = (getSafeStringCell(row, 1) != null) ? getSafeStringCell(row, 1) : ConstantCategory;
                            if(row.getRowNum() < 15 || row.getCell(2) == null || row.getCell(3) == null ) continue;

                            localProduct.setIdFromDistributor(getSafeStringCell(row, 0));
                            localProduct.setNombre(getSafeStringCell(row, 2));
                            localProduct.setPrecioLista(getSafeNumericCell(row, 3));
                            localProduct.setCategoria(ConstantCategory);
                            break;

                        case "oscar":
                            localProduct.setIdFromDistributor(getSafeStringCell(row, 0));
                            localProduct.setNombre(getSafeStringCell(row, 1));
                            localProduct.setCategoria(getSafeStringCell(row, 2));
                            localProduct.setPrecioLista( getSafeNumericCell(row, 3));
                            break;

                        case "atlantica":
                            //categorias divididas en el excel en partes, entonces se trabaja para unirlas y persistirlas durante la carga de elementos de dicha categoria
                            if(ConstantCategory != null && ConstantCategory.isEmpty())
                                ConstantCategory = getSafeStringCell(row, 0) != null ? getSafeStringCell(row, 0) : "";
                            else if(row.getCell(1) == null)
                                SecondCategory = getSafeStringCell(row, 0) != null ? getSafeStringCell(row, 0) : "";

                            //hace un ofset si son las columnas del inicio. Cambia de categora al finalizarla y se limpia la variable de categoria.
                            if(row.getRowNum() <= 3 || (getSafeStringCell(row, 1) != null && Objects.equals(getSafeStringCell(row, 1), "Registro/s"))) {
                                ConstantCategory = "";
                                continue;
                            }
                            if(getSafeNumericCell(row, 3) == 0)
                                continue;

                            localProduct.setIdFromDistributor(getSafeStringCell(row, 0));
                            localProduct.setNombre(getSafeStringCell(row, 1));
                            localProduct.setPrecioLista(getSafeNumericCell(row, 3));
                            localProduct.setCategoria(ConstantCategory + " " + SecondCategory);
                            break;

                        case "foxs":
                            localProduct.setNombre(getSafeStringCell(row, 1));
                            localProduct.setPrecioLista(getSafeNumericCell(row, 2));
                            localProduct.setIdFromDistributor( getSafeStringCell(row, 0));
                            break;

                        default:
                            throw new RuntimeException(); //todo crear excepcion de error a la hora de encontrar la lista en la logica de datos.
                    }

                    if (localProduct.getNombre() != null && !localProduct.getNombre().isEmpty() && localProduct.getPrecioLista() != 0) {
                        groupindex += 1;

                        productsBuffer.add(localProduct);
                        localProduct.clear();
                    }
                    if(groupindex == 50)
                    {
                        productRepo.saveAll(productsBuffer);

                        productsBuffer.clear();

                        groupindex = 0;
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException("error procesando la fila : " + row.getRowNum() + "se detuvo el proceso.");
                }
            }
            return "Archivo procesado exitosamente";
        } catch (Exception e) {
            return "Error de procesamiento";
        }
    }
    private String getSafeStringCell(Row row, int index) { //todo cambiar los return null por strings vacios para omitir condicionales en la logica de Deserialize()
        try {
            if (row.getCell(index) == null) return null;

            return switch (row.getCell(index).getCellType()) {
                case STRING, FORMULA -> row.getCell(index).getStringCellValue().trim();
                case NUMERIC -> String.valueOf((int) row.getCell(index).getNumericCellValue());
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }

    private float getSafeNumericCell(Row row, int index) {
        try {
            if (row.getCell(index) == null) return 0;

            return switch (row.getCell(index).getCellType()) {
                case NUMERIC, FORMULA -> (float) row.getCell(index).getNumericCellValue();
                case STRING ->
                        {
                            String raw = row.getCell(index).getStringCellValue().trim();
                            NumberFormat format = NumberFormat.getInstance(new Locale("es", "AR")); //todo Actualizar metodo deprecated
                            Number number = format.parse(raw);
                            yield number.floatValue();
                        }
                default -> 0;
            };
        } catch (Exception e) {
            return 0;
        }
    }
}
