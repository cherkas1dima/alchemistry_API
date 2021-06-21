package com.alchemistry.dto.modelsdto;

import com.alchemistry.entities.Elixir;
import com.alchemistry.entities.IngredientType;
import com.alchemistry.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "anIngredientDto", toBuilder = true)
public class IngredientDto {

    private String name;
    private IngredientType type;
    private Long cost;
    private List<Elixir> elixirs;
    private List<User> ingredientOwners;
}
