package com.alchemistry.transformers;

import java.util.List;
import java.util.stream.Collectors;

public interface Transformer<E, D> {

    D entityToDto(E entity);

    E dtoToEntity(D dto);

    default List<D> entityToDto(List<E> entityList) {
        return entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
    default List<E> dtoToEntity(List<D> dtoList) {
        return dtoList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
