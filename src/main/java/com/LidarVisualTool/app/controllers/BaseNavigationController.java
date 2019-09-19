package com.LidarVisualTool.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseNavigationController {
    @GetMapping(value = "/")
    public String index() {
        return "index";
    }
}
