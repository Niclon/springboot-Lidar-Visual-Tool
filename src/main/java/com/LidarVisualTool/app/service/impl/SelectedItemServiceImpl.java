package com.LidarVisualTool.app.service.impl;

import com.LidarVisualTool.app.dto.SavePictureRequestDto;
import com.LidarVisualTool.app.dto.SavedPictureResponseDto;
import com.LidarVisualTool.app.dto.SelectedDataRequestDto;
import com.LidarVisualTool.app.dto.SelectedDataResponseDto;
import com.LidarVisualTool.app.helper.FileSaverHelper;
import com.LidarVisualTool.app.model.RawData;
import com.LidarVisualTool.app.model.SelectedDataPart;
import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.model.SelectedItemPicture;
import com.LidarVisualTool.app.repository.RawDataStoreRepository;
import com.LidarVisualTool.app.repository.SelectedItemDataPartRepository;
import com.LidarVisualTool.app.repository.SelectedItemNameRepository;
import com.LidarVisualTool.app.repository.SelectedItemPictureRepository;
import com.LidarVisualTool.app.service.api.SelectedItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelectedItemServiceImpl implements SelectedItemService {
    private SelectedItemNameRepository selectedItemNameRepository;
    private SelectedItemDataPartRepository selectedItemDataPartRepository;
    private RawDataStoreRepository rawDataStoreRepository;
    private SelectedItemPictureRepository selectedItemPictureRepository;
    private FileSaverHelper fileSaverHelper;

    public SelectedItemServiceImpl(SelectedItemNameRepository selectedItemNameRepository,
                                   SelectedItemDataPartRepository selectedItemDataPartRepository,
                                   RawDataStoreRepository rawDataStoreRepository,
                                   SelectedItemPictureRepository selectedItemPictureRepository, FileSaverHelper fileSaverHelper) {
        this.selectedItemNameRepository = selectedItemNameRepository;
        this.selectedItemDataPartRepository = selectedItemDataPartRepository;
        this.rawDataStoreRepository = rawDataStoreRepository;
        this.selectedItemPictureRepository = selectedItemPictureRepository;
        this.fileSaverHelper = fileSaverHelper;
    }

    @Override
    public SelectedItemName saveItemNameAndReturnName(String selectedItemName) {
        SelectedItemName selectedItem = selectedItemNameRepository.findByItemNameIgnoreCase(selectedItemName);
        if (selectedItem == null) {
            return selectedItemNameRepository.save(new SelectedItemName(selectedItemName));
        }
        return selectedItem;
    }

    @Override
    public void saveItemDataPartsFromDto(List<SelectedDataRequestDto> selectedDataRequestDtos) {
        for (SelectedDataRequestDto dataPartDto : selectedDataRequestDtos) {
            Long rawDataId = rawDataStoreRepository.findIdByMainMenuIdAndStepNumber(dataPartDto.getMenuId(), dataPartDto.getStepNumber());
            SelectedDataPart selectedDataPart = new SelectedDataPart();
            selectedDataPart.setRawData(new RawData(rawDataId));
            selectedDataPart.setRawSelectedDataArray(dataPartDto.getRawSelectedDataWithLine());
            selectedDataPart.setSelectedItemName(dataPartDto.getSelectedItemNameObject());
            selectedDataPart.setSelectedItemPicture(selectedItemPictureRepository.getOne(dataPartDto.getPictureId()));
            selectedItemDataPartRepository.save(selectedDataPart);
        }
    }

    @Override
    public List<SavedPictureResponseDto> saveItemPictureAndReturn(List<SavePictureRequestDto> savePictureRequestDto) {
        List<SavedPictureResponseDto> resultList = new ArrayList<>();

        for (SavePictureRequestDto pictureRequestDto : savePictureRequestDto) {
            SavedPictureResponseDto savedPictureResponseDto = new SavedPictureResponseDto();
            SelectedItemPicture newItemPicture = new SelectedItemPicture();

            newItemPicture.setPicturePath(
                    fileSaverHelper.createImageAndReturnPathFromBase64(
                            pictureRequestDto.getPictureInBase64()));
            newItemPicture = selectedItemPictureRepository.save(newItemPicture);

            savedPictureResponseDto.setCameraHashFromFrontEnd(pictureRequestDto.getCameraHashUuID());
            savedPictureResponseDto.setNewlySavedPictureId(newItemPicture.getId());
            resultList.add(savedPictureResponseDto);
        }

        return resultList;
    }

    @Override
    public List<SelectedDataResponseDto> getAllSelectedDataPartsForMenuIdAndDataPartId(Long menuId, Long dataId) {
        List<String> allSelectedDataPartsForMenuIdAndDataPartId = selectedItemDataPartRepository.getAllSelectedDataPartsForMenuIdAndDataPartId(menuId, dataId);
        List<SelectedDataResponseDto> responseDtoList = new ArrayList<>();
        for (String dataPart : allSelectedDataPartsForMenuIdAndDataPartId) {
            responseDtoList.add(new SelectedDataResponseDto(dataPart));
        }

        return responseDtoList;
    }

    @Override
    public List<SelectedItemName> getAllSelectedItemNames() {
        return selectedItemNameRepository.findAll();
    }
}
