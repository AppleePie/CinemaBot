package server;

import models.Film;
import models.Genre;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DatabaseHelper {
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

    public ArrayList<Film> readFilmWithYear(String year) {
        Session session = null;
        ArrayList<Film> filmsThisYear = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            filmsThisYear = (ArrayList<Film>) session.createCriteria(Film.class)
                    .list()
                    .stream()
                    .filter(f -> ((Film) f).getFullReleaseDate().contains(year))
                    .collect(Collectors.toCollection(ArrayList<String>::new));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return filmsThisYear;
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

    public ArrayList<String> getAllAvailableGenresAsStrings() {
        Session session = null;
        Transaction ts = null;
        ArrayList<String> allGenres = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            ts = session.beginTransaction();
            allGenres = (ArrayList<String>) session.createCriteria(Genre.class)
                    .list()
                    .stream()
                    .map(g -> ((Genre) g).getValue())
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList<String>::new));
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

        return allGenres;
    }

    public ArrayList<String> getAllAvailableReleaseYearsAsStrings() {
        Session session = null;
        Transaction ts = null;
        ArrayList<String> allReleaseYears = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            ts = session.beginTransaction();
            allReleaseYears = (ArrayList<String>) session.createCriteria(Film.class)
                    .list()
                    .stream()
                    .map(f -> findYear(((Film) f).getFullReleaseDate()))
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList<String>::new));
            allReleaseYears.sort(Collections.reverseOrder());
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

        return allReleaseYears;
    }

    private String findYear(String date) {
        var pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(date);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new IllegalArgumentException("Date should have year!");
    }
}
