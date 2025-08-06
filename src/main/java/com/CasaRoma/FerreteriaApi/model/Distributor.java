package com.CasaRoma.FerreteriaApi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "distribuidores")
public class Distributor {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "augment")
    private float aumento;

    public Distributor() {
    }

    public Distributor(Integer id, String name, float aumento) {
        this.id = id;
        this.name = name;
        this.aumento = aumento;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAumento() {
        return aumento;
    }

    public void setAumento(float aumento) {
        this.aumento = aumento;
    }
}
