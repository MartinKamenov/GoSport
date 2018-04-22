package com.kamenov.martin.gosportbg.repositories;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class GenericCacheRepository<T, K> {
    private final AbstractDao<T, K> mDao;

    /**
     * Creates a {@link GenericCacheRepository} instance
     *
     * @param dao a GreenDao instance to work with SQLite
     */
    public GenericCacheRepository(AbstractDao<T, K> dao) {
        mDao = dao;
    }

    public List<T> getAll() {
        List<T> itemsList = mDao.loadAll();
        return itemsList;
    }

    public T add(T obj) {
        mDao.insert(obj);
        return obj;
    }

    public void clearAll() {
        mDao.deleteAll();
    }
}
