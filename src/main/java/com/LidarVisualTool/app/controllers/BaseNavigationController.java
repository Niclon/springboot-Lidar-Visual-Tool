package com.LidarVisualTool.app.controllers;

import com.LidarVisualTool.app.repository.MainMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseNavigationController {
    private final MainMenuRepository mainMenuRepository;

    public BaseNavigationController(MainMenuRepository mainMenuRepository) {
        this.mainMenuRepository = mainMenuRepository;
    }

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/menu")
    public String mainMenu(Model model) {
        model.addAttribute("mainMenuitems", mainMenuRepository.findAll());
        return "mainMenu";
    }
}
