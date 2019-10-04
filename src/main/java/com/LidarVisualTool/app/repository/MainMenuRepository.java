package com.LidarVisualTool.app.repository;

import com.LidarVisualTool.app.model.MainMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainMenuRepository extends JpaRepository<MainMenu, Long> {
    List<MainMenu> findAll();
}
