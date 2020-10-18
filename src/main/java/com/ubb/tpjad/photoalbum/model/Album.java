package com.ubb.tpjad.photoalbum.model;

import javax.persistence.*;

@Entity
public class Album {
    @Id
    @GeneratedValue
    private long id;
    private String name;
}
