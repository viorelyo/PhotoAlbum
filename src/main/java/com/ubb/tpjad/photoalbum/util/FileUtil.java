package com.ubb.tpjad.photoalbum.util;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.io.InputStream;

public interface FileUtil {
    String store(InputStream fileStream, String filename) throws IOException;
    ByteArrayResource load(String filePath);
}
