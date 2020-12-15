package com.ubb.tpjad.photoalbum.controller;

import com.ubb.tpjad.photoalbum.model.Photo;
import com.ubb.tpjad.photoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("albumId") int albumId) {
        Photo photo = photoService.storeFile(file, albumId);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("photoId") int photoId) {
        Resource photo = photoService.loadFile(photoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + photo.getFilename() + "\"")
                .body(photo);
    }

    @DeleteMapping("/remove")
    public void removeFile(@RequestParam("photoId") int photoId) {
        Photo photo = photoService.removeFile(photoId);
    }

    @GetMapping("/getphotosbyalbum")
    @ResponseBody
    public List<Photo> getPhotos(@RequestParam("albumId") int albumId) {
        List<Photo> photos = photoService.getPhotosByAlbum(albumId);
        return photos;
    }

    @GetMapping("/filtered")
    @ResponseBody
    public List<Photo> getPhotosFilterByDate(@RequestParam("albumId") int albumId,
                                             @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                             @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return photoService.getPhotosByAlbumFilterByDate(albumId, from, to);
    }

    @GetMapping("/sorted")
    @ResponseBody
    public List<Photo> getPhotosFilterByDate(@RequestParam("albumId") int albumId, @RequestParam("asc") boolean ascending) {
        return photoService.getPhotosByAlbumSortByDate(albumId, ascending);
    }
}
