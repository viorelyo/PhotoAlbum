package com.ubb.tpjad.photoalbum.service;

import com.ubb.tpjad.photoalbum.model.Photo;
import com.ubb.tpjad.photoalbum.response.PhotoResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface PhotoService {
    Photo storeFile(MultipartFile file, int albumId);
    Resource loadFile(int photoId);
    Photo removeFile(int photoId);
    List<PhotoResponse> getPhotosByAlbum(int albumId);
    List<PhotoResponse> getPhotosByAlbumFilterAndSort(int albumId, LocalDate from, LocalDate to, Boolean ascending);
    List<PhotoResponse> getCompressedPhotos(List<Photo> photos);
    PhotoResponse getPhotoResponse(Photo photo);
}
