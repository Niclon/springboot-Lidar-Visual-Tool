package com.LidarVisualTool.app.service.api;

import com.LidarVisualTool.app.model.SelectedItemName;

public interface SelectedItemService {
    SelectedItemName saveItemNameAndReturnName(String selectedItemName);
}
