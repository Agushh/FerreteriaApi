package com.CasaRoma.FerreteriaApi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Tabla de Productos")
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "idFromDistributor")
    private String idFromDistributor;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Categoria")
    private String categoria;

    @Column(name = "Precio de Lista")
    private float precioLista;

    @JoinColumn(name = "distributorID")
    @ManyToOne
    private Distributor distributor;


    //dudoso todo revisar si realmente se necesita
    @Transient
    private Integer idOfDistributor;

    public Integer getIdOfDistributor() {
        return idOfDistributor;
    }


    public Product() {
    }

    public Product(Integer id, String idFromDistributor, String nombre, String descripcion, float precio, Distributor distributor) {
        this.id = id;
        this.idFromDistributor = idFromDistributor;
        this.nombre = nombre;
        this.categoria = descripcion;
        this.precioLista = precio;
        this.distributor = distributor;
    }

    public String getIdFromDistributor() {
        return idFromDistributor;
    }

    public void setIdFromDistributor(String idFromDistributor) {
        this.idFromDistributor = idFromDistributor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getPrecioLista() {
        return precioLista;
    }

    public void setPrecioLista(float precioLista) {
        this.precioLista = precioLista;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
