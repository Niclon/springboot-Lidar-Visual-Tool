package com.LidarVisualTool.app.service.api;

import com.LidarVisualTool.app.dto.dataExport.DataExportDto;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface DataExportService {
     List<DataExportDto> getAllSelectedDataToExport();

     List<DataExportDto> getSelectedDataToExport(Long mainMenuId, Date date);

     File createZipFileForExport(List<DataExportDto> exportData) throws IOException;
}
