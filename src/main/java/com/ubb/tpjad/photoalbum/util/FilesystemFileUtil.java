package com.ubb.tpjad.photoalbum.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
    public Resource load(String filePath) throws FileNotFoundException {
        try {
            log.info("Loading file: [{}]", filePath);
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.warn("Could not find file: [{}]", filePath);
                throw new FileNotFoundException("Could not find file");
            }
        } catch (MalformedURLException ex) {
            log.warn(ex.getMessage());
            throw new FileNotFoundException("Could not load file");
        }
    }
}
