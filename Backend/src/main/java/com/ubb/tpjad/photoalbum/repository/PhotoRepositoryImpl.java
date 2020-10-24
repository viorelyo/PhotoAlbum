package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Photo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

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

    @Override
    public Optional<Photo> getPhotoById(int id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Photo> query = cb.createQuery(Photo.class);

        Root<Photo> root = query.from(Photo.class);
        query.select(root).where(cb.equal(root.get("id"), id));

        return session.createQuery(query).getResultList().stream().findFirst();
    }

    @Override
    public void removePhoto(Photo photo) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.delete(photo);

        session.getTransaction().commit();
    }
}
