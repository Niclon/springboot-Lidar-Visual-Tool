package com.LidarVisualTool.app.dto;

import com.LidarVisualTool.app.model.SelectedItemName;

public class SelectedDataRequestDto {
//    main_menu_storage_id
    private Long menuId;
//    data_id
    private Long stepNumber;
//    pictureId
    private Long pictureId;
    private SelectedItemName selectedItemNameObject;
    private String rawSelectedDataWithLine;

    public Long getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Long stepNumber) {
        this.stepNumber = stepNumber;
    }

    public SelectedItemName getSelectedItemNameObject() {
        return selectedItemNameObject;
    }

    public void setSelectedItemNameObject(SelectedItemName selectedItemNameObject) {
        this.selectedItemNameObject = selectedItemNameObject;
    }

    public String getRawSelectedDataWithLine() {
        return rawSelectedDataWithLine;
    }

    public void setRawSelectedDataWithLine(String rawSelectedDataWithLine) {
        this.rawSelectedDataWithLine = rawSelectedDataWithLine;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }
}
