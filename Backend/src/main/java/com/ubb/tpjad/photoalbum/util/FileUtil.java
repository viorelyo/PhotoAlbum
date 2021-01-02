package com.ubb.tpjad.photoalbum.util;

import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileUtil {
    String store(InputStream fileStream, String dirName, String filename) throws IOException;
    Resource load(String filePath) throws FileNotFoundException;
    byte[] loadBytes(String filePath) throws FileNotFoundException;
    void remove(String filePath) throws FileNotFoundException;
    String storeCompressedPhoto(InputStream fileStream, String dirName, String filename) throws IOException;

    void createDirectory(String dirName) throws SecurityException;
}
