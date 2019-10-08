package com.LidarVisualTool.app.repository;

import com.LidarVisualTool.app.model.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDataStoreRepository extends JpaRepository<RawData, Long> {
    Long countByMainMenu_Id(Long menuId);

    @Query(value = "SELECT r.rawDataArray FROM RawData r WHERE r.mainMenu.id = ?1 AND r.dataId = ?2")
    String getRawDataByMainMenuIdAndDataId(Long mainMenuId, Long dataId);
}