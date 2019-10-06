package com.LidarVisualTool.app.controllers;

import com.LidarVisualTool.app.repository.MainMenuRepository;
import com.LidarVisualTool.app.repository.RawDataStoreRepository;
import com.LidarVisualTool.app.service.api.RawDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BaseNavigationController {
    private final MainMenuRepository mainMenuRepository;
    private final RawDataService rawDataService;

    public BaseNavigationController(MainMenuRepository mainMenuRepository, RawDataService rawDataService) {
        this.mainMenuRepository = mainMenuRepository;
        this.rawDataService = rawDataService;
    }

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("mainMenuitems", mainMenuRepository.findAll());

        return "mainMenu";
    }

    @GetMapping(value = "/load/data/{id}")
    public String mainMenu(Model model, @PathVariable(value = "id") Long id) {
        model.addAttribute("menuId", id);
        model.addAttribute("maxOfSlider", rawDataService.getNumberOfDataAvailable(id));

        return "index";
    }
}
