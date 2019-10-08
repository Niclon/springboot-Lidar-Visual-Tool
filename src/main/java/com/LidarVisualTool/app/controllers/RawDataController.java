package com.LidarVisualTool.app.controllers;

import com.LidarVisualTool.app.exception.EntityNotFoundException;
import com.LidarVisualTool.app.service.api.RawDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RawDataController {

    @Autowired
    RawDataService rawDataService;

    @GetMapping(value = "/rawData/{menuId}/{dataId}")
    public String getRawDataForVisualization(
            @PathVariable(value = "menuId") Long menuId,
            @PathVariable(value = "dataId") Long dataId)
            throws EntityNotFoundException {
        return rawDataService.getRawDataForVisualization(menuId, dataId);
    }
}
