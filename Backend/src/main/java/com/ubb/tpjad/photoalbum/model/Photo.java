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
    private Album albumId;

    @Column(unique = true)
    @NonNull
    private String name;

    @NonNull
    private Date date;

    @NonNull
    private String filePath;

    @NonNull
    private String thumbnailPath;

    public Photo(@NonNull Album albumId, @NonNull String name, @NonNull Date date, @NonNull String filePath, @NonNull String thumbnailPath) {
        this.albumId = albumId;
        this.name = name;
        this.date = date;
        this.filePath = filePath;
        this.thumbnailPath = thumbnailPath;
    }
}
