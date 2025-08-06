package com.CasaRoma.FerreteriaApi.model;

public class ProductDTO {
    final int id;
    final String idFromTheDistributor;
    final int idOfTheDistributor;
    final String nombre;
    final String categoria;
    final float precioLista;
    final float precioFinal;


    public ProductDTO(int id, String idFromTheDistributor, int idOfTheDistributor, String nombre, String categoria, float precioLista, float aumento)
    {
        this.id = id;
        this.idFromTheDistributor = idFromTheDistributor;
        this.idOfTheDistributor = idOfTheDistributor;
        this.categoria = categoria;
        this.nombre = nombre;
        this.precioLista = precioLista;
        this.precioFinal = ( aumento / 100 + 1 ) * precioLista;
    }

    public ProductDTO(Product product)
    {
        id = product.getId();
        idFromTheDistributor = product.getIdFromDistributor();
        if(product.getDistributor() != null) idOfTheDistributor = product.getDistributor().getId();
        else idOfTheDistributor = 0;
        nombre = product.getNombre();
        categoria = product.getCategoria();
        precioLista = product.getPrecioLista();
        if(product.getDistributor() != null) precioFinal = ( product.getDistributor().getAumento() / 100 + 1 ) * product.getPrecioLista();
        else precioFinal = product.getPrecioLista();
    }

    public int getIdOfTheDistributor() {
        return idOfTheDistributor;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public int getId() {
        return id;
    }

    public String getIdFromTheDistributor() {
        return idFromTheDistributor;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecioLista() {
        return precioLista;
    }

    public float getPrecioFinal() {
        return precioFinal;
    }
}
