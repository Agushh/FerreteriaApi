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
import java.util.Locale;
import java.util.Objects;

@Service
public class ExcelService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private DistributorRepo distributorRepo;

    public String deserialize(MultipartFile file, Integer distributorID) {

        Distributor distributor = distributorRepo.findById(distributorID).orElseThrow(() -> new ResourceNotFoundException("Distribuidor no encontrado."));

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            String ConstantCategory = "";
            String SecondCategory = "";
            for (Row row : sheet) {

                try {

                    if (row.getRowNum() == 0) continue;

                    String nombre = "";
                    String categoria = "";
                    String idFromDistributor = "";
                    float precioLista = 0;

                    switch (distributor.getName().toLowerCase()) {
                        case "sg electricidad":

                            break;

                        case "poxipol":
                            if (row.getCell(1) == null || row.getCell(3) == null) continue; //categoria puede ser nula
                            nombre = getSafeStringCell(row, 1);
                            categoria = getSafeStringCell(row, 2);
                            precioLista = getSafeNumericCell(row, 3);
                            idFromDistributor = "";
                            break;

                        case "nexo":
                            if(row.getCell(1) != null) ConstantCategory = getSafeStringCell(row, 1) != null ? getSafeStringCell(row, 1) : ConstantCategory;
                            if(row.getRowNum() < 15 || row.getCell(2) == null || row.getCell(3) == null ) continue;

                            idFromDistributor = getSafeStringCell(row, 0);
                            nombre = getSafeStringCell(row, 2);
                            precioLista = getSafeNumericCell(row, 3);
                            categoria = ConstantCategory;
                            break;

                        case "oscar":
                            idFromDistributor = getSafeStringCell(row, 0);
                            nombre = getSafeStringCell(row, 1);
                            categoria = getSafeStringCell(row, 2);
                            precioLista = getSafeNumericCell(row, 3);
                            break;

                        case "atlantica":
                            if(ConstantCategory != null && ConstantCategory.isEmpty()) ConstantCategory = getSafeStringCell(row, 0) != null ? getSafeStringCell(row, 0) : "";
                            else if(row.getCell(1) == null) SecondCategory = getSafeStringCell(row, 0) != null ? getSafeStringCell(row, 0) : "";
                            if(row.getRowNum() <= 3 || getSafeStringCell(row, 1) != null && Objects.equals(getSafeStringCell(row, 1), "Registro/s")) {
                                ConstantCategory = "";
                                continue;
                            }
                            if(getSafeNumericCell(row, 2) == 0) continue;
                            idFromDistributor = getSafeStringCell(row, 0);
                            nombre = getSafeStringCell(row, 1);
                            precioLista = getSafeNumericCell(row, 3);
                            categoria = ConstantCategory + " " + SecondCategory;
                            break;

                        case "foxs":
                            nombre = getSafeStringCell(row, 1);
                            precioLista = getSafeNumericCell(row, 2);
                            idFromDistributor = getSafeStringCell(row, 0);
                            break;

                        default:
                            throw new RuntimeException();
                    }

                    if (nombre != null && !nombre.isEmpty() && precioLista != 0) {
                        productRepo.save(new Product(null, idFromDistributor, nombre, categoria != null ? categoria : "", precioLista, distributor));
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException("error procesando la fila : " + row.getRowNum() + "se detuvo el proceso.");
                }
            }
            return "Archivo procesado exitosamente";
        } catch (Exception e) {
            return "Error a la hora de encontrar el distribuidor";
        }
    }
    private String getSafeStringCell(Row row, int index) {
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
