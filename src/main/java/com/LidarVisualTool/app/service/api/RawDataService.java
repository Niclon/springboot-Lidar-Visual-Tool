package com.LidarVisualTool.app.service.api;

import com.LidarVisualTool.app.enums.RawDataPicturePosition;
import com.LidarVisualTool.app.exception.EntityNotFoundException;

public interface RawDataService {
    Long getNumberOfDataAvailable(Long menuId);
    String getRawDataForVisualization(Long menuId, Long dataId) throws EntityNotFoundException;
    String getPathForPicture(Long menuId, Long dataId, RawDataPicturePosition position) throws EntityNotFoundException;
}
