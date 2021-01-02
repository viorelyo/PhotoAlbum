package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Album;
import com.ubb.tpjad.photoalbum.model.Photo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
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

    @Override
    public List<Photo> getPhotosByAlbum(Album album) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Photo> query = cb.createQuery(Photo.class);

        Root<Photo> root = query.from(Photo.class);
        query.select(root).where(cb.equal(root.get("albumId"), album.getId()));

        return session.createQuery(query).getResultList();
    }

    @Override
    public List<Photo> getPhotosByAlbumFilterAndSort(Album album, LocalDate from, LocalDate to, Boolean ascending) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Photo> query = cb.createQuery(Photo.class);

        Root<Photo> root = query.from(Photo.class);
        Predicate whereCondition = cb.equal(root.get("albumId"), album.getId());

        // check if filter by date parameters where passed
        if (from != null && to != null) {
            Predicate dateBetweenCondition = cb.between(root.get("date"), Date.valueOf(from), Date.valueOf(to));
            whereCondition = cb.and(whereCondition, dateBetweenCondition);
        }

        query.select(root).where(whereCondition);

        // check if sort by date (ascending boolean) parameter was passed
        if (ascending != null) {
            Order sortingOrder = ascending ? cb.asc(root.get("date")) : cb.desc(root.get("date"));
            query.orderBy(sortingOrder);
        }

        return session.createQuery(query).getResultList();
    }
}
