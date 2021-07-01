package com.alchemistry.conrollers;

import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.dto.modelsdto.UserDto;
import com.alchemistry.dto.requsestdto.CombineIngredientsRequest;
import com.alchemistry.facade.AlchemyFacade;
import com.alchemistry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.alchemistry.utils.AlchemistryContants.MAGISTER_ALCHEMIST;

@RestController
@RequestMapping(value = "/alchemy/api/v1/user/")
public class UserRestController {

    private final UserService userService;
    private final AlchemyFacade alchemyFacade;

    @Autowired
    public UserRestController(UserService userService, AlchemyFacade alchemyService) {
        this.userService = userService;
        this.alchemyFacade = alchemyService;
    }

    @GetMapping("recipes")
    public ResponseEntity<List<List<IngredientDto>>> getAvailableRecipes() {
        try {
            return ResponseEntity.ok(alchemyFacade.getAvailableRecipes());
        } catch (Exception e) {
            throw new RuntimeException("Getting available recipes ERROR");
        }
    }

    @Secured(MAGISTER_ALCHEMIST)
    @GetMapping("list")
    public ResponseEntity<List<UserDto>> getAll() {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            throw new RuntimeException("Getting user List ERROR");
        }
    }

    @GetMapping("id/{user_id}")
    public ResponseEntity<UserDto> getById(@PathVariable(value = "user_id") String id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (Exception e) {
            throw new RuntimeException("Getting user by id ERROR");
        }
    }

    @GetMapping("name/{user_name}")
    public ResponseEntity<UserDto> getByName(@PathVariable(value = "user_name") String name) {
        try {
            return ResponseEntity.ok(userService.getByName(name));
        } catch (Exception e) {
            throw new RuntimeException("Getting user by name ERROR");
        }
    }

    @Secured(MAGISTER_ALCHEMIST)
    @DeleteMapping("delete/{user_id}")
    public ResponseEntity.BodyBuilder delete(@PathVariable(value = "user_id") String id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Deleting user ERROR");
        }
    }

    @PutMapping("buy/{ingredient_name}/{ingredients_number}")
    public ResponseEntity.BodyBuilder buyIngredient(@PathVariable(value = "ingredient_name") String name,
                                                    @PathVariable(value = "ingredients_number") Integer number) {
        try {
            alchemyFacade.buyIngredient(name, number);
            return ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Buying ingredient ERROR");
        }
    }

    @PutMapping("sell/ingredient/{ingredient_name}/{ingredients_number}")
    public ResponseEntity.BodyBuilder sellIngredient(@PathVariable(value = "ingredient_name") String name,
                                                     @PathVariable(value = "ingredients_number") Integer number) {
        try {
            alchemyFacade.sellIngredient(name, number);
            return ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Selling ingredient ERROR");
        }
    }

    @PostMapping("mix")
    public ResponseEntity<IngredientDto> makeElixir(@RequestBody CombineIngredientsRequest items) {
        try {
            return ResponseEntity.ok(alchemyFacade.makeElixir(items));
        } catch (Exception e) {
            throw new RuntimeException("Making elixir ERROR");
        }
    }
}
