package com.ubb.tpjad.photoalbum.controller;

import com.ubb.tpjad.photoalbum.model.Photo;
import com.ubb.tpjad.photoalbum.response.PhotoResponse;
import com.ubb.tpjad.photoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
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

    @PostMapping("/uploadMultiple")
    public void uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("albumId") int albumId) {
        Arrays.asList(files).forEach(file -> uploadFile(file, albumId));
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("photoId") int photoId) {
        Resource photo = photoService.loadFile(photoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + photo.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo);
    }

    @DeleteMapping("/remove")
    public void removeFile(@RequestParam("photoId") int photoId) {
        Photo photo = photoService.removeFile(photoId);
    }

    @GetMapping("/")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<PhotoResponse> getPhotos(@RequestParam("albumId") int albumId) {
        return photoService.getPhotosByAlbum(albumId);
    }

    @GetMapping("/filterAndSort")
    @ResponseBody
    public List<PhotoResponse> getPhotosFilterAndSort(@RequestParam("albumId") int albumId,
                                                      @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                      @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                      @RequestParam(name = "asc", required = false) Boolean ascending) {
        return photoService.getPhotosByAlbumFilterAndSort(albumId, from, to, ascending);
    }
}
