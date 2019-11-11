package com.LidarVisualTool.app.controllers;

import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.service.api.SelectedItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelectedItemController {
    private final SelectedItemService selectedItemService;

    public SelectedItemController(SelectedItemService selectedItemService) {
        this.selectedItemService = selectedItemService;
    }

    @PostMapping("/selectedItem/save/name")
    public ResponseEntity<SelectedItemName> saveSelectedItemNameBasedOnName(@RequestBody String selectedItemName){
        return new ResponseEntity<>(selectedItemService.saveItemNameAndReturnName(selectedItemName), HttpStatus.OK);
    }
}
