package com.ubb.tpjad.photoalbum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue
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
}
