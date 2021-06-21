package com.alchemistry.conrollers;

import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.dto.requsestdto.IngredientsFilterRequest;
import com.alchemistry.services.IngredientService;
import com.alchemistry.utils.AlchemySortParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/alchemy/api/v1/ingredient/")
public class IngredientRestController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientRestController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("list")
    public ResponseEntity<List<IngredientDto>> getAll() {
        try {
            return ResponseEntity.ok(ingredientService.getAll());
        } catch (Exception e) {
            throw new RuntimeException("Getting ingredient List ERROR");
        }
    }

    @GetMapping("id/{ingredient_id}")
    public ResponseEntity<IngredientDto> getById(@PathVariable(value = "ingredient_id") String id) {
        try {
            return ResponseEntity.ok(ingredientService.getById(id));
        } catch (Exception e) {
            throw new RuntimeException("Getting ingredient by id ERROR");
        }
    }

    @GetMapping("name/{ingredient_name}")
    public ResponseEntity<IngredientDto> getByName(@PathVariable(value = "ingredient_name") String name) {
        try {
            return ResponseEntity.ok(ingredientService.getByName(name));
        } catch (Exception e) {
            throw new RuntimeException("Getting ingredient by name ERROR");
        }
    }

    @GetMapping("sort/{parameter}")
    public ResponseEntity<List<IngredientDto>> sortBy(@PathVariable(value = "parameter") AlchemySortParams parameter) {
        try {
            if (parameter == null) parameter = AlchemySortParams.NAME;
            return ResponseEntity.ok(ingredientService.sortBy(parameter));
        } catch (Exception e) {
            throw new RuntimeException("Sorting ingredients by parameter: " + parameter + "; ERROR");
        }
    }

    @PostMapping("filter")
    public ResponseEntity<List<IngredientDto>> filterBy(
            @RequestBody IngredientsFilterRequest parameter) {
        try {
            List<IngredientDto> result;
            if (parameter.getName() != null) result = ingredientService.filterBy(parameter.getName());
            else if (parameter.getType() != null) result = ingredientService.filterBy(parameter.getType());
            else if (parameter.getCost() != null && parameter.getMoreThanCost() != null)
                result = ingredientService.filterByCost(parameter.getCost(), parameter.getMoreThanCost());
            else throw new RuntimeException("Filtering ingredients by parameter: " + parameter + "; ERROR");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException("Filtering ingredients by parameter: " + parameter + "; ERROR");
        }
    }
}
