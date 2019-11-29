package com.LidarVisualTool.app.helper;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class FileSaverHelper {
    @Value("${selectedItem.image.folder.path}")
    private String pathToSelectedItemImageFolder;

    public String createImageAndReturnPathFromBase64(String imageData) {
        byte[] data = Base64.decodeBase64(imageData.substring(22));
        String fileName = UUID.randomUUID().toString() + ".png";
        String resultFilePath = pathToSelectedItemImageFolder + fileName;
        try (OutputStream stream = new FileOutputStream(resultFilePath)) {
            try {
                stream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultFilePath;
    }
}
