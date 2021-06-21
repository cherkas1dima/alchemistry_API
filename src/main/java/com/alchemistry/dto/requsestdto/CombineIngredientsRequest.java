package com.alchemistry.dto.requsestdto;

import com.alchemistry.entities.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombineIngredientsRequest {

    @Size(min = 2, max = 3)
    private List<Ingredient> ingredients;
}
