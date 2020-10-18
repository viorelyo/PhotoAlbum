package com.ubb.tpjad.photoalbum.repository;

import com.ubb.tpjad.photoalbum.model.Album;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class AlbumRepositoryImpl implements AlbumRepository {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Album> getAlbums() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaQuery<Album> query = session.getCriteriaBuilder().createQuery(Album.class);
        query.select(query.from(Album.class));
        return session.createQuery(query).getResultList();
    }
}
