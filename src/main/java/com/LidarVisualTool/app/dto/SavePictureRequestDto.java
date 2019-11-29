package com.LidarVisualTool.app.dto;

import java.io.Serializable;

public class SavePictureRequestDto implements Serializable {
    private String cameraHashUuID;
    private String pictureInBase64;

    public SavePictureRequestDto() {
    }

    public String getCameraHashUuID() {
        return cameraHashUuID;
    }

    public void setCameraHashUuID(String cameraHashUuID) {
        this.cameraHashUuID = cameraHashUuID;
    }

    public String getPictureInBase64() {
        return pictureInBase64;
    }

    public void setPictureInBase64(String pictureInBase64) {
        this.pictureInBase64 = pictureInBase64;
    }
}
