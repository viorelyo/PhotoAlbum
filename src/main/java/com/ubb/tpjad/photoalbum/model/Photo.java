package com.ubb.tpjad.photoalbum.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Photo {
    @Id
    @GeneratedValue
    private int id;
    private int albumId;
    private String name;
    private Date date;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] file;
}
