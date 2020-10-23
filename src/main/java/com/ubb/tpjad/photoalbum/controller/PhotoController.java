package com.ubb.tpjad.photoalbum.controller;

import com.ubb.tpjad.photoalbum.model.Photo;
import com.ubb.tpjad.photoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
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
}
