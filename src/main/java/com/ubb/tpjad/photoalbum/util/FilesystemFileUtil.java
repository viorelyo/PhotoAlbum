package com.ubb.tpjad.photoalbum.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Slf4j
public class FilesystemFileUtil implements FileUtil {

    @Value("${app.uploadDir}")
    public String uploadDir;

    @Override
    public String store(InputStream fileStream, String filename) throws IOException {
        try {
            Path fileLocation = Paths.get(uploadDir + File.separator + filename);
            log.info("Storing file: [{}]", fileLocation.toString());
            Files.copy(fileStream, fileLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileLocation.toString();
        } catch (IOException ex) {
            log.warn(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ByteArrayResource load(String filePath) {
        return null;
    }
}
