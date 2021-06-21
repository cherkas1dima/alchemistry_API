package com.alchemistry.dto.modelsdto;

import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "anElixirDto", toBuilder = true)
public class ElixirDto {

    private String name;
    private Long cost;
    private Integer level;
    private List<Ingredient> recipe;
    private List<User> elixirOwners;
}
