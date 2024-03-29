package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Album;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class AlbumRepositoryImpl implements AlbumRepository {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Album> getAlbums() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaQuery<Album> query = session.getCriteriaBuilder().createQuery(Album.class);

        Root<Album> root = query.from(Album.class);
        query.select(root);

        return session.createQuery(query).getResultList();
    }

    @Override
    public Optional<Album> getAlbumById(int id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Album> query = cb.createQuery(Album.class);

        Root<Album> root = query.from(Album.class);
        query.select(root).where(cb.equal(root.get("id"), id));

        return session.createQuery(query).getResultList().stream().findFirst();
    }

    @Override
    public Album save(Album album) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.save(album);

        session.getTransaction().commit();

        return album;
    }
}
