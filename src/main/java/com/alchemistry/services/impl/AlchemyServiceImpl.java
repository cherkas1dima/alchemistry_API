package com.alchemistry.services.impl;

import com.alchemistry.dto.modelsdto.ElixirDto;
import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.dto.modelsdto.UserDto;
import com.alchemistry.dto.requsestdto.CombineIngredientsRequest;
import com.alchemistry.entities.Elixir;
import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.User;
import com.alchemistry.entities.UserRoles;
import com.alchemistry.repos.UserRepository;
import com.alchemistry.repos.UserRolesRepository;
import com.alchemistry.services.AlchemyService;
import com.alchemistry.services.ElixirService;
import com.alchemistry.services.IngredientService;
import com.alchemistry.transformers.Transformer;
import com.alchemistry.utils.AlchemyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.alchemistry.entities.IngredientType.SOLID;
import static com.alchemistry.utils.AlchemistryContants.SIMPLE_ALCHEMIST;
import static com.alchemistry.utils.AlchemistryContants.USER_SERVICE_LOGGER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlchemyServiceImpl implements AlchemyService {

    private final Transformer<Ingredient, IngredientDto> ingredientTransformer;
    private final Transformer<Elixir, ElixirDto> elixirTransformer;
    private final UserRepository userRepository;
    private final UserRolesRepository rolesRepository;
    private final ElixirService elixirService;
    private final IngredientService ingredientService;

    @Override
    public void buyIngredient(String name) {
        User user = AlchemyUtils.setRole(getAlchemyRole(), AlchemyUtils.getUserFromContextHolder());
        IngredientDto ingredient = ingredientService.getByName(name);
        if (user.getCoins().compareTo(ingredient.getCost()) >= 0) {
            user.getIngredients().add(ingredientTransformer.dtoToEntity(ingredient));
            user.setCoins(user.getCoins() - ingredient.getCost());
            userRepository.save(user);
            log.info("{} was bought by {}; for - {}", name, user.getName(), USER_SERVICE_LOGGER);
        } else log.info("NOT ENOUGH COINS ; for - {}", USER_SERVICE_LOGGER);
    }

    @Override
    public void sellElixir(String name) {
        User user = AlchemyUtils.setRole(getAlchemyRole(), AlchemyUtils.getUserFromContextHolder());
        Elixir elixir = elixirTransformer.dtoToEntity(elixirService.getByName(name));
        if (AlchemyUtils.ifRemoveItem(Collections.singletonList(user.getElixirs()), elixir)) {
            user.setCoins(user.getCoins() + elixir.getCost());
            userRepository.save(user);
            log.info("Elixir {} was sold ; for - {}", name, USER_SERVICE_LOGGER);
        } else log.info("You have no such elixir: {}; for - {}", name, USER_SERVICE_LOGGER);
    }

    @Override
    public void sellIngredient(String name) {
        User user = AlchemyUtils.setRole(getAlchemyRole(), AlchemyUtils.getUserFromContextHolder());
        Ingredient ingredient = ingredientTransformer.dtoToEntity(ingredientService.getByName(name));
        if (AlchemyUtils.ifRemoveItem(Collections.singletonList(user.getIngredients()), ingredient)) {
            user.setCoins(user.getCoins() + ingredient.getCost());
            userRepository.save(user);
            log.info("Ingredient {} was sold ; for - {}", name, USER_SERVICE_LOGGER);
        } else log.info("You have no such ingredient: {}; for - {}", name, USER_SERVICE_LOGGER);
    }

    @Override
    public ElixirDto makeElixir(CombineIngredientsRequest items) {
        User user = AlchemyUtils.setRole(getAlchemyRole(), AlchemyUtils.getUserFromContextHolder());
        if (isItemsMatchIngredients(items.getIngredients(), user.getIngredients())) {
            Elixir resultElixir = combineIngredients(items.getIngredients());
            if (resultElixir != null) {
                user.getElixirs().add(resultElixir);
                consumeSuccessfulUsedIngredients(items.getIngredients(), user.getIngredients());
                userRepository.save(user);
                log.info("User {} successfully created {} elixir; for - {}",
                        user.getName(), resultElixir.getName(), USER_SERVICE_LOGGER);
                return elixirTransformer.entityToDto(resultElixir);
            } else {
                consumeUnsuccessfulUsedIngredients(items.getIngredients(), user.getIngredients());
                userRepository.save(user);
                log.info("Unsuccessful making elixir; for - {}", USER_SERVICE_LOGGER);
                return null;
            }
        }
        log.info("User {} hasn't such ingredients; for - {}", user.getName(), USER_SERVICE_LOGGER);
        return null;
    }

    private UserRoles getAlchemyRole() {
        return rolesRepository.findByName(SIMPLE_ALCHEMIST);
    }

    private Elixir combineIngredients(List<Ingredient> combination) {
        List<Elixir> elixirList = elixirTransformer.dtoToEntity(elixirService.getAll());
        return elixirList.stream()
                .map(elixir -> matchWithRecipes(elixir, combination))
                .findFirst().orElse(null);
    }

    private Elixir matchWithRecipes(Elixir elixir, List<Ingredient> combination) {
        List<Ingredient> matchResult = elixir.getRecipe()
                .stream()
                .filter(combination::contains)
                .collect(Collectors.toList());
        if (matchResult.size() == combination.size()) return elixir;
        return null;
    }

    private boolean isItemsMatchIngredients(List<Ingredient> combination, List<Ingredient> ingredients) {
        List<Ingredient> matchResult = ingredients
                .stream()
                .filter(combination::contains)
                .collect(Collectors.toList());
        return matchResult.size() == combination.size();
    }

    private void consumeSuccessfulUsedIngredients(
            List<Ingredient> combination, List<Ingredient> ingredients) {
        combination.forEach(c -> ingredients.removeIf(i -> i.equals(c)));
    }

    private void consumeUnsuccessfulUsedIngredients(
            List<Ingredient> combination, List<Ingredient> ingredients) {
        combination.forEach(c -> ingredients.removeIf(i -> i.equals(c) && !c.getType().equals(SOLID)));
    }
}
