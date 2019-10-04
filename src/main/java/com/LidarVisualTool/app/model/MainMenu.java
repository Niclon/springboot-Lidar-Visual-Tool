package com.LidarVisualTool.app.model;

import jdk.jfr.Enabled;

import javax.persistence.*;

@Entity
@Table(name = "main_menu_storage")
public class MainMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification_name")
    private String identificationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificationName() {
        return identificationName;
    }

    public void setIdentificationName(String identificationName) {
        this.identificationName = identificationName;
    }
}
