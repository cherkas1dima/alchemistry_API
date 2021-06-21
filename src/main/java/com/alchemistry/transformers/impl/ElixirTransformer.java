package com.alchemistry.transformers.impl;

import com.alchemistry.dto.modelsdto.ElixirDto;
import com.alchemistry.entities.Elixir;
import com.alchemistry.transformers.Transformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElixirTransformer implements Transformer<Elixir, ElixirDto>{

    @Override
    public ElixirDto entityToDto(Elixir entity) {
        return ElixirDto.anElixirDto()
                .setName(entity.getName())
                .setCost(entity.getCost())
                .setLevel(entity.getLevel())
                .setRecipe(entity.getRecipe())
                .setElixirOwners(entity.getElixirOwners())
                .build();
    }

    @Override
    public Elixir dtoToEntity(ElixirDto dto) {
        return Elixir.anElixir()
                .setName(dto.getName())
                .setCost(dto.getCost())
                .setLevel(dto.getLevel())
                .setRecipe(dto.getRecipe())
                .setElixirOwners(dto.getElixirOwners())
                .build();
    }
}