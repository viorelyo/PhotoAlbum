package com.ubb.tpjad.photoalbum.service;

import com.ubb.tpjad.photoalbum.exception.BadRequestException;
import com.ubb.tpjad.photoalbum.exception.EntityNotFoundException;
import com.ubb.tpjad.photoalbum.exception.FileStorageException;
import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.model.Photo;
import com.ubb.tpjad.photoalbum.repository.AlbumRepository;
import com.ubb.tpjad.photoalbum.repository.PhotoRepository;
import com.ubb.tpjad.photoalbum.response.PhotoResponse;
import com.ubb.tpjad.photoalbum.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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

        if (filename.contains("..") || filename.contains("/") || filename.contains("\\") || filename.startsWith(".")) {
            log.warn(String.format("Filename contains illegal characters: [%s]", filename));
            throw new FileStorageException(String.format("Invalid path: [%s].", filename));
        }

        try {
            log.info("Getting album by id");
            Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
            if (!foundAlbum.isPresent()) {
                log.warn("Could not find specified album with id: [{}]", albumId);
                throw new FileStorageException("Could not find specified album.");
            }
            Album album = foundAlbum.get();

            String thumbnailPath = fileUtil.storeCompressedPhoto(file.getInputStream(), album.getName(), filename);
            String filePath = fileUtil.store(file.getInputStream(), album.getName(), filename);

            Photo photo = new Photo(album, FilenameUtils.getName(filePath), new Date(System.currentTimeMillis()), filePath, thumbnailPath);
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
            log.warn("Could not find specified photo with id: [{}]", photoId);
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
    public Photo removeFile(int photoId) {
        log.info("Removing photo");

        log.info("Getting photo by id");
        Optional<Photo> foundPhoto = photoRepository.getPhotoById(photoId);
        if (!foundPhoto.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", photoId);
            throw new FileStorageException("Could not find specified photo.");
        }
        Photo photo = foundPhoto.get();

        try {
            fileUtil.remove(photo.getFilePath());
        } catch (FileNotFoundException ex) {
            log.warn(ex.getMessage());
            throw new FileStorageException(String.format("Could not remove photo: [%s].", photo.getName()), ex);
        }

        log.info("Removing photo thumbnail");
        try {
            fileUtil.remove(photo.getThumbnailPath());
        } catch (FileNotFoundException ex) {
            log.warn(ex.getMessage());
            throw new FileStorageException(String.format("Could not remove photo thumbnail: [%s].", photo.getName()), ex);
        }

        log.info("Removing photo: [{}] from repo", photo.getName());
        photoRepository.removePhoto(photo);

        return photo;
    }

    @Override
    public List<PhotoResponse> getPhotosByAlbum(int albumId) {
        log.info("Getting photos by album");

        Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
        if (!foundAlbum.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", albumId);
            throw new EntityNotFoundException("Could not find specified album.");
        }
        Album album = foundAlbum.get();

        List<Photo> photos = photoRepository.getPhotosByAlbum(album);
        return getCompressedPhotos(photos);
    }

    @Override
    public List<PhotoResponse> getPhotosByAlbumFilterAndSort(int albumId, LocalDate from, LocalDate to, Boolean ascending) {
        log.info("Getting photos by album id: [{}] from: {} to: {}, sort ascending: {}", albumId, from, to, ascending);

        if (from != null && to != null && from.isAfter(to)) {
            throw new BadRequestException("\"From\" date must be before \"to\" date");
        }

        Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
        if (!foundAlbum.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", albumId);
            throw new EntityNotFoundException("Could not find specified album.");
        }
        Album album = foundAlbum.get();

        List<Photo> photos = photoRepository.getPhotosByAlbumFilterAndSort(album, from, to, ascending);
        return getCompressedPhotos(photos);
    }

    @Override
    public List<PhotoResponse> getCompressedPhotos(List<Photo> photos) {
        List<PhotoResponse> response = new ArrayList<>();
        for (Photo photo : photos) {
            response.add(getPhotoResponse(photo));
        }
        return response;
    }

    @Override
    public PhotoResponse getPhotoResponse(Photo photo) {
        log.info("Creating photo response from photo thumbnail: [{}]", photo.getId());

        try {
            byte[] img = fileUtil.loadBytes(photo.getThumbnailPath());
            return new PhotoResponse(photo.getId(), photo.getName(), photo.getDate(), img);
        } catch (IOException ex) {
            log.warn(ex.getMessage());
            throw new FileStorageException(String.format("Could not load photo: [%s].", photo.getName()), ex);
        }
    }
}
