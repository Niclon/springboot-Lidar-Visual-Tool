package com.LidarVisualTool.app.repository;

import com.LidarVisualTool.app.model.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDataStoreRepository extends JpaRepository<RawData, Long> {
    public Long countByMainMenu_Id(Long menuId);
}