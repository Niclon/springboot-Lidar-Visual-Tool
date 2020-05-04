package com.LidarVisualTool.app.controllers;

import com.LidarVisualTool.app.dto.dataExport.DataExportDto;
import com.LidarVisualTool.app.service.api.DataExportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

@Controller
public class DataExportController {

    private final DataExportService dataExportService;

    public DataExportController(DataExportService dataExportService) {
        this.dataExportService = dataExportService;
    }

    @ResponseBody
    @GetMapping(value = "/download/full/export", produces = "application/zip")
    public ResponseEntity<InputStreamResource> downloadSelectedData() throws IOException {
        List<DataExportDto> allSelectedDataToExport = dataExportService.getAllSelectedDataToExport();
        File resultZipFile = dataExportService.createZipFileForExport(allSelectedDataToExport);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resultZipFile.getName())
                .body(new InputStreamResource(new ByteArrayInputStream(Files.readAllBytes(resultZipFile.toPath()))));
    }

    @ResponseBody
    @GetMapping(value = "/download/export", produces = "application/zip")
    public ResponseEntity<InputStreamResource> downloadSelectedData(@RequestParam("mainMenuId") Long mainMenuId,
                                                                    @RequestParam("dateInMilis") Long dateInMilis) throws IOException {
        Date date = new Date(dateInMilis);
        List<DataExportDto> allSelectedDataToExport = dataExportService.getSelectedDataToExport(mainMenuId, date);
        File resultZipFile = dataExportService.createZipFileForExport(allSelectedDataToExport);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resultZipFile.getName())
                .body(new InputStreamResource(new ByteArrayInputStream(Files.readAllBytes(resultZipFile.toPath()))));
    }

}
