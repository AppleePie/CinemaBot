package server;

import models.Film;
import models.Genre;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Random;

public class DataHelper {
    public void insertOne(Film film) {
        Session session = null;
        Transaction ts = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            ts = session.beginTransaction();
            session.save(film);
            ts.commit();
        } catch (Exception e) {
            if (ts != null) {
                ts.rollback();
            }
            System.err.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void insertMany(List<Film> films) {
        for (var film : films) {
            insertOne(film);
        }
    }

    public Film readFilmWithGenre(Genre genre) {
        Session session = null;
        Film film = null;
        final Random rnd = new Random();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            var allFilms = session.createCriteria(Film.class)
                    .list()
                    .stream()
                    .filter(f -> ((Film) f).getGenres().contains(genre))
                    .toArray();
            film = (Film) allFilms[rnd.nextInt(allFilms.length)];
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return film;
    }

    public Film readRandomFilm() {
        Session session = null;
        Film film = null;
        var filmId = new Random().nextInt(250);
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            film = session.get(Film.class, filmId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return film;
    }

    public void clearTable() {
        Session session = null;
        Transaction ts = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            ts = session.beginTransaction();
            var allFilms = session.createCriteria(Film.class).list();
            for (var obj : allFilms) {
                session.delete(obj);
            }
            ts.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());

            if (ts != null) {
                ts.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
