package com.ubb.tpjad.photoalbum.service;

import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.repository.AlbumRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public List<Album> getAlbums() {
        log.info("Getting all albums");
        return albumRepository.getAlbums();
    }

    @Override
    public Album addAlbum(String name) {
        log.info("Adding album");

        Album album = new Album(name);

        albumRepository.save(album);

        return album;
    }
}
