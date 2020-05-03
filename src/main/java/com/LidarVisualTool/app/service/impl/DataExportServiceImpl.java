package com.LidarVisualTool.app.service.impl;

import com.LidarVisualTool.app.dto.dataExport.DataExportDto;
import com.LidarVisualTool.app.dto.dataExport.DataExportRow;
import com.LidarVisualTool.app.model.SelectedDataPart;
import com.LidarVisualTool.app.model.SelectedItemName;
import com.LidarVisualTool.app.repository.SelectedItemDataPartRepository;
import com.LidarVisualTool.app.repository.SelectedItemNameRepository;
import com.LidarVisualTool.app.service.api.DataExportService;
import com.LidarVisualTool.app.util.ZipUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataExportServiceImpl implements DataExportService {

    //    todo maybe remove
    private final SelectedItemNameRepository selectedItemNameRepository;
    private final SelectedItemDataPartRepository selectedItemDataPartRepository;

    @Value("${export.tmp.folder.path}")
    private String exportTmpFolderPath;

    @Value("${selectedItem.image.folder.path}")
    private String selectedItemPicturesFolderPath;

    public DataExportServiceImpl(SelectedItemNameRepository selectedItemNameRepository, SelectedItemDataPartRepository selectedItemDataPartRepository) {
        this.selectedItemNameRepository = selectedItemNameRepository;
        this.selectedItemDataPartRepository = selectedItemDataPartRepository;
    }

    @Override
    public List<DataExportDto> getAllSelectedDataToExport() {
        List<DataExportDto> result = new ArrayList<>();
        List<SelectedItemName> all = selectedItemNameRepository.findAll();

        for (SelectedItemName selectedItemName : all) {
            DataExportDto item = new DataExportDto();
            item.setItemName(selectedItemName.getItemName());
            List<SelectedDataPart> allBySelectedItemName =
                    selectedItemDataPartRepository.getAllBySelectedItemName(selectedItemName.getId());
            DataExportRow row;
            for (SelectedDataPart selectedDataPart : allBySelectedItemName) {
                row = new DataExportRow();
                row.setMenuId(selectedDataPart.getRawData().getMainMenu().getId());
                row.setDataId(selectedDataPart.getRawData().getDataId());
//                // TODO: 5/1/2020 maybe little update path
                String[] splits = selectedDataPart.getSelectedItemPicture().getPicturePath().split("/");
                row.setImageName(splits[splits.length - 1]);
//                // TODO: 5/1/2020 remove line from it
                row.setPoints(selectedDataPart.getRawSelectedDataArray());
                item.addRowToData(row);
            }
            result.add(item);
        }

        return result;
    }

    @Override
    public File createZipFileForExport(List<DataExportDto> exportData) throws IOException {
        String finalZipPathToFile = exportTmpFolderPath + new Date().toString() + ".zip";
        String finalPathToTmpFile = exportTmpFolderPath + "resultData";
        File tmpHolder = new File(finalPathToTmpFile);
        tmpHolder.mkdirs();
        ObjectMapper objectMapper = new ObjectMapper();

        for (DataExportDto dataExportDto : exportData) {
            String itemName = dataExportDto.getItemName();
            String itemFolder = finalPathToTmpFile + "/" + itemName;
            String pictureItemFolder = itemFolder + "/pictures";
            new File(pictureItemFolder).mkdirs();
            for (DataExportRow dataRow : dataExportDto.getData()) {
                FileUtil.copyFile(
                        new File(selectedItemPicturesFolderPath + dataRow.getImageName()),
                        new File(pictureItemFolder + dataRow.getImageName())
                );
            }
            objectMapper.writeValue(new File(itemFolder + "/data.json"), dataExportDto);
        }

        try {
            ZipUtil.zipFolder(finalPathToTmpFile, finalZipPathToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileUtils.deleteDirectory(new File(finalPathToTmpFile));

        return new File(finalZipPathToFile);
    }
}
