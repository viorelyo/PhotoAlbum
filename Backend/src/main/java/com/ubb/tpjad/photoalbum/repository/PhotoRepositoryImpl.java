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
    public List<Photo> getPhotosByAlbumFilterByDate(Album album, LocalDate from, LocalDate to) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Photo> query = cb.createQuery(Photo.class);

        Root<Photo> root = query.from(Photo.class);
        Predicate whereAlbumIdEqualsCondition = cb.equal(root.get("albumId"), album.getId());
        Predicate dateBetweenCondition = cb.between(root.get("date"), Date.valueOf(from), Date.valueOf(to));
        query.select(root).where(cb.and(whereAlbumIdEqualsCondition, dateBetweenCondition));

        return session.createQuery(query).getResultList();
    }

    @Override
    public List<Photo> getPhotosByAlbumSortByDate(Album album, boolean ascending) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Photo> query = cb.createQuery(Photo.class);

        Root<Photo> root = query.from(Photo.class);
        Order sortingOrder = ascending ? cb.asc(root.get("date")) : cb.desc(root.get("date"));
        query.select(root).where(cb.equal(root.get("albumId"), album.getId())).orderBy(sortingOrder);

        return session.createQuery(query).getResultList();
    }
}
