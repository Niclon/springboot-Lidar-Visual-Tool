package com.LidarVisualTool.app.repository;

import com.LidarVisualTool.app.model.RawDataPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDataPicturesRepository extends JpaRepository<RawDataPicture, Long> {
    RawDataPicture findByRawData_DataIdAndRawData_MainMenu_Id(Long rawDataPositionId, Long mainMenuId);
}

