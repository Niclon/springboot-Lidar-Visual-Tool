package com.LidarVisualTool.app.service.impl;

import com.LidarVisualTool.app.enums.RawDataPicturePosition;
import com.LidarVisualTool.app.exception.EntityNotFoundException;
import com.LidarVisualTool.app.model.RawDataPicture;
import com.LidarVisualTool.app.repository.RawDataPicturesRepository;
import com.LidarVisualTool.app.repository.RawDataStoreRepository;
import com.LidarVisualTool.app.service.api.RawDataService;
import org.springframework.stereotype.Service;

@Service
public class RawDataServiceImpl implements RawDataService {
    private final RawDataStoreRepository rawDataStoreRepository;
    private final RawDataPicturesRepository rawDataPicturesRepository;

    public RawDataServiceImpl(RawDataStoreRepository rawDataStoreRepository, RawDataPicturesRepository rawDataPicturesRepository) {
        this.rawDataStoreRepository = rawDataStoreRepository;
        this.rawDataPicturesRepository = rawDataPicturesRepository;
    }

    @Override
    public Long getNumberOfDataAvailable(Long menuId) {
        return rawDataStoreRepository.countByMainMenu_Id(menuId);
    }

    @Override
    public String getRawDataForVisualization(Long menuId, Long dataId) throws EntityNotFoundException {
        String rawData = rawDataStoreRepository.getRawDataByMainMenuIdAndDataId(menuId, dataId);
        if (rawData == null) {
            throw new EntityNotFoundException();
        }
        return rawData;
    }

    @Override
    public String getPathForPicture(Long menuId, Long dataId, RawDataPicturePosition position) throws EntityNotFoundException {
        RawDataPicture rawDataPicture = rawDataPicturesRepository.findByRawData_DataIdAndRawData_MainMenu_Id(dataId, menuId);
        if (rawDataPicture == null) {
            throw new EntityNotFoundException();
        }
        return position.equals(RawDataPicturePosition.FRONT_PICTURE) ?
                rawDataPicture.getFrontPicturePath() : rawDataPicture.getBackPicturePath();
    }
}
