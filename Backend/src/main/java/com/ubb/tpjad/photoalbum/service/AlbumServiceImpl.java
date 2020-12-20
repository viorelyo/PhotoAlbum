package com.ubb.tpjad.photoalbum.service;

import com.ubb.tpjad.photoalbum.exception.FileStorageException;
import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.repository.AlbumRepository;
import com.ubb.tpjad.photoalbum.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public List<Album> getAlbums() {
        log.info("Getting all albums");
        return albumRepository.getAlbums();
    }

    @Override
    public Album addAlbum(String name) {
        log.info("Adding album");

        // TODO check for other vulnerabilities (Windows paths related maybe)
        if (name.contains("..")) {
            log.warn(String.format("Album name contains illegal characters: [%s]", name));
            throw new FileStorageException(String.format("Invalid album name: [%s].", name));
        }

        try {
            fileUtil.createDirectory(name);
        } catch (SecurityException ex) {
            throw new FileStorageException(String.format("Could not create album directory : [%s].", name), ex);
        }

        Album album = new Album(name);
        albumRepository.save(album);

        return album;
    }
}
