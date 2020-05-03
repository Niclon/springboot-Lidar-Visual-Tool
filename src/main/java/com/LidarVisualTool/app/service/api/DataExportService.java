package com.LidarVisualTool.app.service.api;

import com.LidarVisualTool.app.dto.dataExport.DataExportDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DataExportService {
     List<DataExportDto> getAllSelectedDataToExport();
     File createZipFileForExport(List<DataExportDto> exportData) throws IOException;
}
