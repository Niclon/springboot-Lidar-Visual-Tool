package com.LidarVisualTool.app.dto;

import java.io.Serializable;

public class SelectedDataResponseDto implements Serializable {
    private String rawSelectedData;

    public SelectedDataResponseDto(String rawSelectedData) {
        this.rawSelectedData = rawSelectedData;
    }

    public String getRawSelectedData() {
        return rawSelectedData;
    }

    public void setRawSelectedData(String rawSelectedData) {
        this.rawSelectedData = rawSelectedData;
    }
}
