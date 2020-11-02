package Server;

import Models.Film;
import org.hibernate.Session;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class DataHelper {
    public void insertOne(Film film) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(film);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка при вставке", JOptionPane.ERROR_MESSAGE);
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

    public Film readRandomFilm() {
        Session session = null;
        Film film = null;
        var filmId = new Random().nextInt(250);
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            film = session.get(Film.class, filmId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка при вставке", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return film;
    }

    public void clearTable() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            var allFilms = session.createCriteria(Film.class).list();
            for (var obj : allFilms) {
                session.delete(obj);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'findById'", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
