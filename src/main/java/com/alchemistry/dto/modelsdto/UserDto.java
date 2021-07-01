package com.alchemistry.dto.modelsdto;

import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "anUserDto", toBuilder = true)
public class UserDto {

    private String name;
    private String e_mail;
    private String password;
    private Long coins;
    private List<Ingredient> ingredients;
    private List<Recipe> unlockedRecipes;
}
