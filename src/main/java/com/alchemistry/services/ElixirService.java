package com.alchemistry.services;

import com.alchemistry.dto.modelsdto.ElixirDto;
import com.alchemistry.utils.AlchemySortParams;

import java.util.List;

public interface ElixirService {

    ElixirDto getById(String id);

    ElixirDto getByName(String name);

    List<ElixirDto> getAll();

    void delete(String id);

    List<ElixirDto> sortBy(AlchemySortParams parameter);

    List<ElixirDto> filterBy(Object parameter);

    List<ElixirDto> filterByCost(Long cost, Boolean moreThanCost);
}
