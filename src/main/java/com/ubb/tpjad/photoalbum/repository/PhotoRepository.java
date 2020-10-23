package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Photo;

import java.util.Optional;

public interface PhotoRepository {
    Photo save(Photo photo);
    Optional<Photo> getPhotoById(int id);
}
