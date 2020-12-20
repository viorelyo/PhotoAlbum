package com.ubb.tpjad.photoalbum.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;

@Component
@Slf4j
public class FilesystemFileUtil implements FileUtil {

    @Value("${app.uploadDir}")
    public String uploadDir;

    @Override
    public String store(InputStream fileStream, String dirName, String filename) throws IOException {
        try {
            Path fileLocation = Paths.get(getNewFilePath(uploadDir + File.separator + dirName, filename));
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

    @Override
    public void remove(String filePath) throws FileNotFoundException {
        try {
            log.info("Removing file: [{}]", filePath);
            Path path = Paths.get(filePath);

            Files.delete(path);
            log.warn("File removed successfully");
        } catch (NoSuchFileException ex) {
            log.warn(ex.getMessage());
            throw new FileNotFoundException("Could not find file");
        } catch (IOException ex) {
            log.warn(ex.getMessage());
            throw new FileNotFoundException(ex.getMessage());
        }
    }

    @Override
    public void createDirectory(String dirName) throws SecurityException {
        try {
            log.info("Creating directory: [{}]", dirName);
            File dir = new File(uploadDir + File.separator + dirName);

            if (dir.exists()) {
                log.warn("Directory already exists");
                throw new SecurityException("Directory already exists");
            }
            if (!dir.mkdirs()) {
                log.warn("Could not create directory");
                throw new SecurityException("Could not create directory");
            }
        } catch (SecurityException ex) {
            log.warn(ex.getMessage());
            throw ex;
        }
    }

    public String getNewFilePath(String dirName, String filename) {
        log.info("Handling duplicate filenames");
        String filePath = dirName + File.separator + filename;
        File file = new File(filePath);
        int fileNr = 0;
        String newFilePath = "";
        if (file.exists() && !file.isDirectory()) {
            while(file.exists()){
                fileNr++;
                newFilePath = dirName + File.separator + FilenameUtils.removeExtension(filename) + "(" + fileNr + ")." + FilenameUtils.getExtension(filename);
                file = new File(newFilePath);
            }
        } else if (!file.exists()) {
            newFilePath = filePath;
        }
        return newFilePath;
    }
}
