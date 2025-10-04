package com.learning.learn03.base;

public interface BaseMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
