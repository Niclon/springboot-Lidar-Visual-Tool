package com.LidarVisualTool.app.service.impl;

import com.LidarVisualTool.app.exception.EntityNotFoundException;
import com.LidarVisualTool.app.model.RawData;
import com.LidarVisualTool.app.repository.RawDataStoreRepository;
import com.LidarVisualTool.app.service.api.RawDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawDataServiceImpl implements RawDataService {
    @Autowired
    RawDataStoreRepository rawDataStoreRepository;

    @Override
    public Long getNumberOfDataAvailable(Long menuId) {
        return rawDataStoreRepository.countByMainMenu_Id(menuId);
    }

    @Override
    public String getRawDataForVisualization(Long menuId, Long dataId) throws EntityNotFoundException {
        String rawData = rawDataStoreRepository.getRawDataByMainMenuIdAndDataId(menuId, dataId);
        if (rawData == null){
            throw new EntityNotFoundException();
        }
        return rawData;
    }
}
