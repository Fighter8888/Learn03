package com.learning.learn03.base;

public interface BaseMapper<Entity, Dto> {
    Dto toDto(Entity entity);
    Entity toEntity(Dto dto);
}