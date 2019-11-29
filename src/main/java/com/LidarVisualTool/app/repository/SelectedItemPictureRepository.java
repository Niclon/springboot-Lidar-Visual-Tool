package com.LidarVisualTool.app.repository;

import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.model.SelectedItemPicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedItemPictureRepository extends JpaRepository<SelectedItemPicture, Long> {
}
