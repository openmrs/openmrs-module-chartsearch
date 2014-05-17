package org.openmrs.module.chartsearch.api.db.hibernate;

/**
 * Created by Eli on 16/05/14.
 */
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.chartsearch.api.db.SingleClassDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class HibernateSingleClassDAO<T> implements SingleClassDAO<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected Class<T> mappedClass;

    /**
     * Marked private because you *must* provide the class at runtime when instantiating one of
     * these, using the next constructor
     */
    @SuppressWarnings("unused")
    private HibernateSingleClassDAO() {
    }

    /**
     * You must call this before using any of the data access methods, since it's not actually
     * possible to write them all with compile-time class information.
     *
     * @param mappedClass
     */
    protected HibernateSingleClassDAO(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public T getById(Integer id) {
        return (T) sessionFactory.getCurrentSession().get(mappedClass, id);
    }



    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return (List<T>) sessionFactory.getCurrentSession().createCriteria(mappedClass).list();
    }

    @Override
    @Transactional
    public T saveOrUpdate(T object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
        return object;
    }


    @Override
    @Transactional
    public void delete(T object) {
        sessionFactory.getCurrentSession().delete(object);
    }
}

