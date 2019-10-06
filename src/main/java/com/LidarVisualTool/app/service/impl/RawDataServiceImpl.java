package com.LidarVisualTool.app.service.impl;

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
}
