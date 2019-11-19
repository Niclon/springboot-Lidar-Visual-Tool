package com.LidarVisualTool.app.service.api;

import com.LidarVisualTool.app.dto.SelectedDataRequestDto;
import com.LidarVisualTool.app.model.SelectedItemName;

import java.util.List;

public interface SelectedItemService {
    SelectedItemName saveItemNameAndReturnName(String selectedItemName);
    void saveItemDataPartsFromDto(List<SelectedDataRequestDto> selectedDataRequestDtos);
}
