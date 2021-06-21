package com.alchemistry.services;

import com.alchemistry.dto.modelsdto.ElixirDto;
import com.alchemistry.dto.requsestdto.CombineIngredientsRequest;
import com.alchemistry.entities.Elixir;

public interface AlchemyService {

    void buyIngredient(String name);

    void sellElixir(String name);

    void sellIngredient(String name);

    ElixirDto makeElixir(CombineIngredientsRequest items);
}
