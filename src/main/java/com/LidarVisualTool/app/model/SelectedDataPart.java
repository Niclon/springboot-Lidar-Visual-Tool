package com.LidarVisualTool.app.model;

import javax.persistence.*;

@Entity
@Table(name = "selected_data_parts")
public class SelectedDataPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "selected_item_name_id", nullable = false)
    private SelectedItemName selectedItemName;

    @OneToOne
    @JoinColumn(name = "selected_item_picture_id", nullable = false)
    private SelectedItemPicture selectedItemPicture;

    @Column(name = "raw_selected_data")
    private String rawSelectedDataArray;

    @ManyToOne
    @JoinColumn(name = "raw_data_id",nullable = false)
    private RawData rawData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SelectedItemName getSelectedItemName() {
        return selectedItemName;
    }

    public void setSelectedItemName(SelectedItemName selectedItemName) {
        this.selectedItemName = selectedItemName;
    }

    public SelectedItemPicture getSelectedItemPicture() {
        return selectedItemPicture;
    }

    public void setSelectedItemPicture(SelectedItemPicture selectedItemPicture) {
        this.selectedItemPicture = selectedItemPicture;
    }

    public String getRawSelectedDataArray() {
        return rawSelectedDataArray;
    }

    public void setRawSelectedDataArray(String rawSelectedDataArray) {
        this.rawSelectedDataArray = rawSelectedDataArray;
    }

    public RawData getRawData() {
        return rawData;
    }

    public void setRawData(RawData rawData) {
        this.rawData = rawData;
    }
}
