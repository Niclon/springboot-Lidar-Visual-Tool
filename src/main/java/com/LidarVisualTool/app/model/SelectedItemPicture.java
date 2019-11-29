package com.LidarVisualTool.app.model;

import javax.persistence.*;

@Entity
@Table(name = "selected_item_pictures")
public class SelectedItemPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture_path")
    private String picturePath;

    public SelectedItemPicture() {
    }

    public SelectedItemPicture(Long pictureId) {
        this.id = pictureId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
