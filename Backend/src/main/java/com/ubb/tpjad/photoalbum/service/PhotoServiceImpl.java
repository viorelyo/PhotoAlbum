package com.ubb.tpjad.photoalbum.service;

import com.ubb.tpjad.photoalbum.exception.FileStorageException;
import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.model.Photo;
import com.ubb.tpjad.photoalbum.repository.AlbumRepository;
import com.ubb.tpjad.photoalbum.repository.PhotoRepository;
import com.ubb.tpjad.photoalbum.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public Photo storeFile(MultipartFile file, int albumId) {
        log.info("Storing photo");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // TODO check for other vulnerabilities
        if (filename.contains("..")) {
            log.warn(String.format("Filename contains illegal characters: [%s]", filename));
            throw new FileStorageException(String.format("Invalid path: [%s].", filename));
        }

        try {
            String filePath = fileUtil.store(file.getInputStream(), filename);

            log.info("Getting album by id");
            Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
            if (!foundAlbum.isPresent()) {
                log.warn("Could not find specified album with id: [{}]", albumId);
                throw new FileStorageException("Could not find specified album.");
            }
            Album album = foundAlbum.get();

            Photo photo = new Photo(album, filename, new Date(System.currentTimeMillis()), filePath);
            log.info("Saving photo: [{}] from album: [{}] to repo", photo.getName(), album.getName());
            return photoRepository.save(photo);
        } catch (IOException ex) {
            log.warn(ex.getMessage());
            throw new FileStorageException(String.format("Could not store file: [%s].", filename), ex);
        }
    }

    @Override
    public Resource loadFile(int photoId) {
        log.info("Loading photo");

        log.info("Getting photo by id");
        Optional<Photo> foundPhoto = photoRepository.getPhotoById(photoId);
        if (!foundPhoto.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", photoId);
            throw new FileStorageException("Could not find specified photo.");
        }
        Photo photo = foundPhoto.get();

        try {
            return fileUtil.load(photo.getFilePath());
        } catch (FileNotFoundException ex) {
            log.warn(ex.getMessage());
            throw new FileStorageException(String.format("Could not load photo: [%s].", photo.getName()), ex);
        }
    }

    @Override
    public List<Photo> getPhotosByAlbum(int albumId) {

        log.info("Getting photos by album");

        Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
        if (!foundAlbum.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", albumId);
            throw new FileStorageException("Could not find specified album.");
        }
        Album album = foundAlbum.get();

        return photoRepository.getPhotosByAlbum(album);
    }

    @Override
    public Photo removeFile(int photoId) {
        log.info("Removing photo");

        log.info("Getting photo by id");
        Optional<Photo> foundPhoto = photoRepository.getPhotoById(photoId);
        if (!foundPhoto.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", photoId);
            throw new FileStorageException("Could not find specified photo.");
        }
        Photo photo = foundPhoto.get();

        log.info("Removing photo: [{}] from repo", photo.getName());
        photoRepository.removePhoto(photo);

        try {
            fileUtil.remove(photo.getFilePath());
        } catch (FileNotFoundException ex) {
            log.warn(ex.getMessage());
            throw new FileStorageException(String.format("Could not remove photo: [%s].", photo.getName()), ex);
        }

        return photo;
    }
}
