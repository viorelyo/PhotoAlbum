package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Photo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PhotoRepositoryImpl implements PhotoRepository {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Photo save(Photo photo) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.save(photo);

        session.getTransaction().commit();
        return photo;
    }
}
