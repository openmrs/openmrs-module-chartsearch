package org.openmrs.module.chartsearch.api.db;

/**
 * Created by Eli on 16/05/14.
 */

import java.util.List;

public interface SingleClassDAO<T> {

    T getById(Integer id);

    List<T> getAll();

    T saveOrUpdate(T object);

    void delete(T object);
}