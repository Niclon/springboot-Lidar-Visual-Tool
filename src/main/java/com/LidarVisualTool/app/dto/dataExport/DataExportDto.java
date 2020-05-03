package com.LidarVisualTool.app.dto.dataExport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataExportDto implements Serializable {
    private String itemName;
    private List<DataExportRow> data;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<DataExportRow> getData() {
        return data;
    }

    public void setData(List<DataExportRow> data) {
        this.data = data;
    }

    public void addRowToData(DataExportRow dataExportRow){
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.add(dataExportRow);
    }
}
