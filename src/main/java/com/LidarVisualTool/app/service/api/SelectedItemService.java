package com.LidarVisualTool.app.service.api;

import com.LidarVisualTool.app.dto.SavePictureRequestDto;
import com.LidarVisualTool.app.dto.SavedPictureResponseDto;
import com.LidarVisualTool.app.dto.SelectedDataRequestDto;
import com.LidarVisualTool.app.dto.SelectedDataResponseDto;
import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.model.SelectedItemPicture;

import java.util.List;

public interface SelectedItemService {
    SelectedItemName saveItemNameAndReturnName(String selectedItemName);

    void saveItemDataPartsFromDto(List<SelectedDataRequestDto> selectedDataRequestDtos);

    List<SavedPictureResponseDto> saveItemPictureAndReturn(List<SavePictureRequestDto> savePictureRequestDto);

    List<SelectedDataResponseDto> getAllSelectedDataPartsForMenuIdAndDataPartId(Long menuId, Long dataId);
}
