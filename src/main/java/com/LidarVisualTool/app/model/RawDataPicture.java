package com.LidarVisualTool.app.model;

import javax.persistence.*;

@Entity
@Table(name = "raw_data_pictures")
public class RawDataPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "front_picture_path")
    private String frontPicturePath;

    @Column(name = "back_picture_path")
    private String backPicturePath;

    @OneToOne
    @JoinColumn(name = "raw_data_id", nullable = false)
    private RawData rawData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrontPicturePath() {
        return frontPicturePath;
    }

    public void setFrontPicturePath(String frontPicturePath) {
        this.frontPicturePath = frontPicturePath;
    }

    public String getBackPicturePath() {
        return backPicturePath;
    }

    public void setBackPicturePath(String backPicturePath) {
        this.backPicturePath = backPicturePath;
    }

    public RawData getRawData() {
        return rawData;
    }

    public void setRawData(RawData rawData) {
        this.rawData = rawData;
    }
}
