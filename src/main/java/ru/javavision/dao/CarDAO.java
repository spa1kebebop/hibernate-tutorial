package ru.javavision.dao;

import org.jetbrains.annotations.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javavision.model.Car;

import java.util.List;

/**
 * Author : Pavel Ravvich.
 * Created : 26/11/2017.
 */
public class CarDAO implements DAO<Car, Integer> {
    /**
     * Connection factory to database.
     */
    private final SessionFactory factory;

    public CarDAO(@NotNull final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void create(@NotNull final Car car) {

        try (final Session session = factory.openSession()) {

            session.beginTransaction();

            session.save(car);

            session.getTransaction().commit();
        }
    }

    @Override
    public Car read(@NotNull final Integer id) {

        try (final Session session = factory.openSession()) {

            final Car result = session.get(Car.class, id);

            if (result != null) {
                Hibernate.initialize(result.getEngine());
            }

            return result;
        }
    }

    @Override
    public void update(@NotNull final Car car) {

        try (Session session = factory.openSession()) {

            session.beginTransaction();

            session.update(car);

            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(@NotNull final Car car) {

        try (Session session = factory.openSession()) {

            session.beginTransaction();

            session.delete(car);

            session.getTransaction().commit();
        }
    }

    private final static String GET_ALL_CARS = "select c from Car c join fetch c.engine where c.id > 0";

    @Override
    public List<Car> getAll() {
        try (Session session = factory.openSession()) {
            return session.createQuery(GET_ALL_CARS, Car.class).list();
        }
    }
}
