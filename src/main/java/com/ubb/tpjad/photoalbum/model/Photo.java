package com.ubb.tpjad.photoalbum.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="albumId", nullable=false)
    @NonNull
    private Album album;

    @Column(unique = true)
    @NonNull
    private String name;

    @NonNull
    private Date date;

    @Lob
    @Column(columnDefinition = "BLOB")
    @NonNull
    private byte[] file;

    public Photo(@NonNull Album album, @NonNull String name, @NonNull Date date, @NonNull byte[] file) {
        this.album = album;
        this.name = name;
        this.date = date;
        this.file = file;
    }
}
