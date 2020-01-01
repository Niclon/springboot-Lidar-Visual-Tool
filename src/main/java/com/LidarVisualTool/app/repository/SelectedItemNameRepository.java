package com.LidarVisualTool.app.repository;

import com.LidarVisualTool.app.model.SelectedItemName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedItemNameRepository extends JpaRepository<SelectedItemName, Long> {
    SelectedItemName findByItemNameIgnoreCase(String itemName);
}
