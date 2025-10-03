package com.learning.learn03.base;

import java.io.Serializable;
import java.util.List;

public interface IBaseService<T extends BaseEntity<ID>, ID extends Serializable> {
    T persist(T t);

    void delete(ID id);

    T findById(ID id);

    List<T> findAll();

    boolean existsById(ID id);
}