package com.LidarVisualTool.app.service.impl;

import com.LidarVisualTool.app.dto.SelectedDataRequestDto;
import com.LidarVisualTool.app.model.RawData;
import com.LidarVisualTool.app.model.SelectedDataPart;
import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.repository.RawDataStoreRepository;
import com.LidarVisualTool.app.repository.SelectedItemDataPartRepository;
import com.LidarVisualTool.app.repository.SelectedItemNameRepository;
import com.LidarVisualTool.app.service.api.SelectedItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectedItemServiceImpl implements SelectedItemService {
    private SelectedItemNameRepository selectedItemRepository;
    private SelectedItemDataPartRepository selectedItemDataPartRepository;
    private RawDataStoreRepository rawDataStoreRepository;

    public SelectedItemServiceImpl(SelectedItemNameRepository selectedItemRepository,
                                   SelectedItemDataPartRepository selectedItemDataPartRepository,
                                   RawDataStoreRepository rawDataStoreRepository) {
        this.selectedItemRepository = selectedItemRepository;
        this.selectedItemDataPartRepository = selectedItemDataPartRepository;
        this.rawDataStoreRepository = rawDataStoreRepository;
    }

    @Override
    public SelectedItemName saveItemNameAndReturnName(String selectedItemName) {
        return selectedItemRepository.save(new SelectedItemName(selectedItemName));
    }

    @Override
    public void saveItemDataPartsFromDto(List<SelectedDataRequestDto> selectedDataRequestDtos) {
        for (SelectedDataRequestDto dataPartDto : selectedDataRequestDtos) {
            Long rawDataId = rawDataStoreRepository.findIdByMainMenuIdAndStepNumber(dataPartDto.getMenuId(), dataPartDto.getStepNumber());
            SelectedDataPart selectedDataPart = new SelectedDataPart();
            selectedDataPart.setRawData(new RawData(rawDataId));
            selectedDataPart.setRawSelectedDataArray(dataPartDto.getRawSelectedDataWithLine());
            selectedDataPart.setSelectedItemName(dataPartDto.getSelectedItemNameObject());
//            todo
//            selectedDataPart.setSelectedItemPictures();
            selectedItemDataPartRepository.save(selectedDataPart);
        }


    }
}
