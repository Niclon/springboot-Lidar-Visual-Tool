package com.LidarVisualTool.app.model;

import javax.persistence.*;

@Entity()
@Table(name = "raw_data_store")
public class RawData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "raw_data_array")
    private String rawDataArray;

    @ManyToOne
    @JoinColumn(name = "main_menu_id")
    private MainMenu mainMenu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRawDataArray() {
        return rawDataArray;
    }

    public void setRawDataArray(String rawDataArray) {
        this.rawDataArray = rawDataArray;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
}
