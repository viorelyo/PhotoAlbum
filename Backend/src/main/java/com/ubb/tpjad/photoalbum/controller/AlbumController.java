package com.ubb.tpjad.photoalbum.controller;

import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Album> getAlbums() {
        return albumService.getAlbums();
    }

    @PostMapping("/add")
    public void addAlbum(@RequestParam("albumName") String name) {
        Album album = albumService.addAlbum(name);
    }
}
