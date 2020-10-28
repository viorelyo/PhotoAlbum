package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Album;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository {

    List<Album> getAlbums();
    Optional<Album> getAlbumById(int id);
    Album save(Album album);
}
