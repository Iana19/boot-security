package com.task_springboot.demo.dao;

import com.task_springboot.demo.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
    public UserDaoImpl() {
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> allUsers() {
        List<User> resultList = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        return resultList;
    }

    @Override
    public void save(User user) {
        User managed = entityManager.merge(user);
        entityManager.persist(managed);
    }

    @Override
    public User getById(Long id) {

        /*
        Так - т.е. без SQL-запроса - полностью корректно работает редактирование!!!
         */
        return entityManager.find(User.class, id );

    }

    @Override
    public User getUserByLogin(String login) {

        try {
            Query query = entityManager.createQuery("select u from User u join fetch u.roles where u.login = :login");
            query.setParameter("login", login);
            return (User) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void delete(User user) {
        User managed = entityManager.merge(user);
        entityManager.remove(managed);
    }
}
