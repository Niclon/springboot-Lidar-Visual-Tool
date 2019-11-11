package com.LidarVisualTool.app.service.impl;

import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.repository.SelectedItemRepository;
import com.LidarVisualTool.app.service.api.SelectedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectedItemServiceImpl implements SelectedItemService {
    @Autowired
    SelectedItemRepository selectedItemRepository;

    @Override
    public SelectedItemName saveItemNameAndReturnName(String selectedItemName) {
        return selectedItemRepository.save(new SelectedItemName(selectedItemName));
    }
}
