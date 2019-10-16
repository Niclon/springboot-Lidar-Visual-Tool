package com.LidarVisualTool.app.model;

import javax.persistence.*;

@Entity
@Table(name = "selected_data_parts")
public class SelectedDataParts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "selected_item_name_id", nullable = false)
    private SelectedItemName selectedItemName;

    @OneToOne
    @JoinColumn(name = "selected_item_picture_id", nullable = false)
    private SelectedItemPictures selectedItemPictures;

    @Column(name = "raw_selected_data")
    private String rawSelectedDataArray;

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

    public SelectedItemPictures getSelectedItemPictures() {
        return selectedItemPictures;
    }

    public void setSelectedItemPictures(SelectedItemPictures selectedItemPictures) {
        this.selectedItemPictures = selectedItemPictures;
    }

    public String getRawSelectedDataArray() {
        return rawSelectedDataArray;
    }

    public void setRawSelectedDataArray(String rawSelectedDataArray) {
        this.rawSelectedDataArray = rawSelectedDataArray;
    }
}