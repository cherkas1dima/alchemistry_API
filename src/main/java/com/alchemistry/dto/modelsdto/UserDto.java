package com.alchemistry.dto.modelsdto;

import com.alchemistry.entities.Elixir;
import com.alchemistry.entities.Ingredient;
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
    private List<Elixir> elixirs;
    private List<Ingredient> ingredients;
}
