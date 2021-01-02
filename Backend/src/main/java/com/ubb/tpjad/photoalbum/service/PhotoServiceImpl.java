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
import com.ubb.tpjad.photoalbum.util.PhotoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.omg.CORBA.portable.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDate;
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
    public List<Photo> getPhotosByAlbum(int albumId) {
        log.info("Getting photos by album");

        Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
        if (!foundAlbum.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", albumId);
            throw new EntityNotFoundException("Could not find specified album.");
        }
        Album album = foundAlbum.get();

        return photoRepository.getPhotosByAlbum(album);
    }

    @Override
    public List<Photo> getPhotosByAlbumFilterByDate(int albumId, LocalDate from, LocalDate to) {
        log.info("Getting photos by album id: [{}] from: {} to: {}", albumId, from, to);

        if (from.isAfter(to)) {
            throw new BadRequestException("\"From\" date must be before \"to\" date");
        }

        Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
        if (!foundAlbum.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", albumId);
            throw new EntityNotFoundException("Could not find specified album.");
        }
        Album album = foundAlbum.get();

        return photoRepository.getPhotosByAlbumFilterByDate(album, from, to);
    }

    @Override
    public List<Photo> getPhotosByAlbumSortByDate(int albumId, boolean ascending) {
        log.info("Getting photos by album id: [{}] sorting by date {}", albumId, ascending ? "ascending" : "descending");

        Optional<Album> foundAlbum = albumRepository.getAlbumById(albumId);
        if (!foundAlbum.isPresent()) {
            log.warn("Could not find specified album with id: [{}]", albumId);
            throw new EntityNotFoundException("Could not find specified album.");
        }
        Album album = foundAlbum.get();

        return photoRepository.getPhotosByAlbumSortByDate(album, ascending);
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
