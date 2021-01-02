package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.model.Photo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    Photo save(Photo photo);
    Optional<Photo> getPhotoById(int id);
    void removePhoto(Photo photo);
    List<Photo> getPhotosByAlbum(Album album);
    List<Photo> getPhotosByAlbumFilterAndSort(Album album, LocalDate from, LocalDate to, Boolean ascending);
}
