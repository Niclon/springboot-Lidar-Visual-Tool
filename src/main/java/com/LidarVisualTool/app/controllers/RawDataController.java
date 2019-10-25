package com.LidarVisualTool.app.controllers;

import com.LidarVisualTool.app.enums.RawDataPicturePosition;
import com.LidarVisualTool.app.exception.EntityNotFoundException;
import com.LidarVisualTool.app.service.api.RawDataService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class RawDataController {

    private final RawDataService rawDataService;

    public RawDataController(RawDataService rawDataService) {
        this.rawDataService = rawDataService;
    }

    @GetMapping(value = "/rawData/{menuId}/{dataId}")
    public String getRawDataForVisualization(
            @PathVariable(value = "menuId") Long menuId,
            @PathVariable(value = "dataId") Long dataId)
            throws EntityNotFoundException {
        return rawDataService.getRawDataForVisualization(menuId, dataId);
    }

    @GetMapping(value = "/rawData/image/front/{menuId}/{dataId}")
    public ResponseEntity<byte[]> getFrontImageDataForRawData(
            @PathVariable(value = "menuId") Long menuId,
            @PathVariable(value = "dataId") Long dataId)
            throws IOException, EntityNotFoundException {
        InputStream in = new FileInputStream(
                rawDataService.getPathForPicture(menuId,dataId, RawDataPicturePosition.FRONT_PICTURE));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(IOUtils.toByteArray(in));
    }

    @GetMapping(value = "/rawData/image/black/{menuId}/{dataId}")
    public ResponseEntity<byte[]> getBackImageDataForRawData(
            @PathVariable(value = "menuId") Long menuId,
            @PathVariable(value = "dataId") Long dataId)
            throws IOException, EntityNotFoundException {
        InputStream in = new FileInputStream(
                rawDataService.getPathForPicture(menuId,dataId, RawDataPicturePosition.BACK_PICTURE));
//        todo maybe png return type
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(IOUtils.toByteArray(in));
    }
}
