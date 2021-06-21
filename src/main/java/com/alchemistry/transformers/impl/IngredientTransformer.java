package com.alchemistry.transformers.impl;

import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.entities.Ingredient;
import com.alchemistry.transformers.Transformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientTransformer implements Transformer<Ingredient, IngredientDto> {

    @Override
    public IngredientDto entityToDto(Ingredient entity) {
        return IngredientDto.anIngredientDto()
                .setName(entity.getName())
                .setType(entity.getType())
                .setCost(entity.getCost())
                .setElixirs(entity.getElixirs())
                .setIngredientOwners(entity.getIngredientOwners())
                .build();
    }

    @Override
    public Ingredient dtoToEntity(IngredientDto dto) {
        return Ingredient.anIngredient()
                .setName(dto.getName())
                .setType(dto.getType())
                .setCost(dto.getCost())
                .setElixirs(dto.getElixirs())
                .setIngredientOwners(dto.getIngredientOwners())
                .build();
    }
}
