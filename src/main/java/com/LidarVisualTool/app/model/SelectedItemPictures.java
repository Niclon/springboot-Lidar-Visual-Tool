package com.LidarVisualTool.app.model;

import javax.persistence.*;

@Entity
@Table(name = "selected_item_pictures")
public class SelectedItemPictures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture_name")
    private String pictureName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }
}
