package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    Photo save(Photo photo);
    Optional<Photo> getPhotoById(int id);
    List<Photo> getPhotosByAlbum(Album album);
    void removePhoto(Photo photo);
}
