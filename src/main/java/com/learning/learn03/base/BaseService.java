package com.learning.learn03.base;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public abstract class BaseService<T extends BaseEntity<ID>, ID extends Serializable>
        implements IBaseService<T, ID> {

    private final String ENTITY_NOT_FOUND = "Entity not found";

    private final JpaRepository<T, ID> repository;

    protected BaseService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T persist(T t) {
        boolean isNew = t.getId() == null;
        if (isNew) {
            prePersist(t);
        } else {
            repository.findById(t.getId())
                    .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
            preUpdate(t);
        }
        T saved = repository.save(t);
        if (isNew) {
            postPersist(saved);
        } else {
            postUpdate(saved);
        }
        return saved;
    }

    @Override
    public void delete(ID id) {
        preDelete(id);
        repository.deleteById(id);
        postDelete(id);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }


    protected void prePersist(T t) {
    }

    protected void postPersist(T t) {
    }

    protected void preUpdate(T t) {
    }

    protected void postUpdate(T t) {
    }

    protected void preDelete(ID id) {
    }

    protected void postDelete(ID id) {
    }

}



