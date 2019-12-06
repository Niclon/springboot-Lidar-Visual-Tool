package com.LidarVisualTool.app.controllers;

import com.LidarVisualTool.app.dto.SavePictureRequestDto;
import com.LidarVisualTool.app.dto.SavedPictureResponseDto;
import com.LidarVisualTool.app.dto.SelectedDataRequestDto;
import com.LidarVisualTool.app.dto.SelectedDataResponseDto;
import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.service.api.SelectedItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SelectedItemController {
    private final SelectedItemService selectedItemService;

    public SelectedItemController(SelectedItemService selectedItemService) {
        this.selectedItemService = selectedItemService;
    }

    @PostMapping("/selectedItem/save/name")
    public ResponseEntity<SelectedItemName> saveSelectedItemNameBasedOnName(@RequestBody String selectedItemName) {
        return new ResponseEntity<>(selectedItemService.saveItemNameAndReturnName(selectedItemName), HttpStatus.OK);
    }

    @PostMapping("/selectedItem/save/data-part")
    public ResponseEntity saveSelectedItemDataParts(@RequestBody List<SelectedDataRequestDto> selectedDataRequestDtos) {
        selectedItemService.saveItemDataPartsFromDto(selectedDataRequestDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/selectedItem/save/camera-images")
    public ResponseEntity<List<SavedPictureResponseDto>> savePicturesFromCameras(
            @RequestBody List<SavePictureRequestDto> savePictureRequestDtos) {
        List<SavedPictureResponseDto> savedPictureResponseDtos = selectedItemService.saveItemPictureAndReturn(savePictureRequestDtos);
        return new ResponseEntity<>(savedPictureResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/selected/data/stored/replay/{menuId}/{dataId}")
    public ResponseEntity<List<SelectedDataResponseDto>> getSelectedDataForVisualization(
            @PathVariable(value = "menuId") Long menuId,
            @PathVariable(value = "dataId") Long dataId) {
        List<SelectedDataResponseDto> response = selectedItemService.getAllSelectedDataPartsForMenuIdAndDataPartId(menuId,dataId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
