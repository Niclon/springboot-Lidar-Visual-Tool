package com.LidarVisualTool.app.dto;

import java.io.Serializable;

public class SavedPictureResponseDto implements Serializable {
    private Long newlySavedPictureId;
    private String cameraHashFromFrontEnd;

    public SavedPictureResponseDto() {
    }

    public Long getNewlySavedPictureId() {
        return newlySavedPictureId;
    }

    public void setNewlySavedPictureId(Long newlySavedPictureId) {
        this.newlySavedPictureId = newlySavedPictureId;
    }

    public String getCameraHashFromFrontEnd() {
        return cameraHashFromFrontEnd;
    }

    public void setCameraHashFromFrontEnd(String cameraHashFromFrontEnd) {
        this.cameraHashFromFrontEnd = cameraHashFromFrontEnd;
    }
}
