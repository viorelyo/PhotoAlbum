package com.ubb.tpjad.photoalbum.service;

import com.ubb.tpjad.photoalbum.model.Photo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    Photo storeFile(MultipartFile file, int albumId);
    Resource loadFile(int photoId);
    Photo removeFile(int photoId);
}
