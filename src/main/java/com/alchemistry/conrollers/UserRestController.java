package com.alchemistry.conrollers;

import com.alchemistry.dto.modelsdto.ElixirDto;
import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.dto.requsestdto.CombineIngredientsRequest;
import com.alchemistry.services.AlchemyService;
import com.alchemistry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/alchemy/api/v1/user/")
public class UserRestController {

    private final UserService userService;
    private final AlchemyService alchemyService;

    @Autowired
    public UserRestController(UserService userService, AlchemyService alchemyService) {
        this.userService = userService;
        this.alchemyService = alchemyService;
    }

    @GetMapping("recipes")
    public ResponseEntity<List<List<IngredientDto>>> getAvailableRecipes() {
        try {
            return ResponseEntity.ok(userService.getAvailableRecipes());
        } catch (Exception e) {
            throw new RuntimeException("Getting available recipes ERROR");
        }
    }

    @GetMapping("buy/{ingredient_name}")
    public ResponseEntity.BodyBuilder buyIngredient(@PathVariable(value = "ingredient_name") String name) {
        try {
            alchemyService.buyIngredient(name);
            return ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Buying ingredient ERROR");
        }
    }

    @GetMapping("sell/ingredient/{ingredient_name}")
    public ResponseEntity.BodyBuilder sellIngredient(@PathVariable(value = "ingredient_name") String name) {
        try {
            alchemyService.sellIngredient(name);
            return ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Selling ingredient ERROR");
        }
    }

    @GetMapping("sell/elixir/{elixir_name}")
    public ResponseEntity.BodyBuilder sellElixir(@PathVariable(value = "elixir_name") String name) {
        try {
            alchemyService.sellElixir(name);
            return ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Selling elixir ERROR");
        }
    }

    @PostMapping("mix")
    public ResponseEntity<ElixirDto> makeElixir(@RequestBody CombineIngredientsRequest items) {
        try {
            return ResponseEntity.ok(alchemyService.makeElixir(items));
        } catch (Exception e) {
            throw new RuntimeException("Making elixir ERROR");
        }
    }
}
