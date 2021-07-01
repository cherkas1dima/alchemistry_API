package com.alchemistry.facade;

import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.dto.requsestdto.CombineIngredientsRequest;
import com.alchemistry.entities.Amount;
import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.IngredientType;
import com.alchemistry.entities.Recipe;
import com.alchemistry.entities.User;
import com.alchemistry.entities.UserRoles;
import com.alchemistry.repos.AmountRepository;
import com.alchemistry.repos.IngredientRepository;
import com.alchemistry.repos.RecipeRepository;
import com.alchemistry.repos.UserRepository;
import com.alchemistry.repos.UserRolesRepository;
import com.alchemistry.services.IngredientService;
import com.alchemistry.transformers.Transformer;
import com.alchemistry.utils.AlchemyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.alchemistry.utils.AlchemistryContants.FACADE_LOGGER;
import static com.alchemistry.utils.AlchemistryContants.SIMPLE_ALCHEMIST;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlchemyFacade {

    private final Transformer<Ingredient, IngredientDto> ingredientTransformer;
    private final UserRepository userRepository;
    private final UserRolesRepository rolesRepository;
    private final AmountRepository amountRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final IngredientRepository ingredientRepository;

    public List<List<IngredientDto>> getAvailableRecipes() {
        User user = AlchemyUtils.setRole(
                rolesRepository.findByName(SIMPLE_ALCHEMIST),
                AlchemyUtils.getUserFromContextHolder()
        );
        return recipeRepository.findByOwners(user).stream()
                .map(Recipe::getRecipes)
                .map(ingredientTransformer::entityToDto)
                .collect(Collectors.toList());
    }

    public void buyIngredient(String name, Integer number) {
        User user = AlchemyUtils.setRole(getAlchemyRole(), AlchemyUtils.getUserFromContextHolder());
        Ingredient ingredient = ingredientRepository.getIngredientByName(name);
        if (user.getCoins().compareTo(ingredient.getCost() * number) >= 0) {
            Integer ingredientAmount = amountRepository
                    .findByUserIdAndIngredientId(user.getId(), ingredient.getId())
                    .getAmount();
            user.getIngredients().add(ingredient);
            user.setCoins(user.getCoins() - ingredient.getCost()* number);
            amountRepository
                    .findByUserIdAndIngredientId(user.getId(), ingredient.getId())
                    .setAmount(ingredientAmount + number);
            userRepository.save(user);
            log.info("{} was bought by {}; for - {}", name, user.getName(), FACADE_LOGGER);
        } else log.info("NOT ENOUGH COINS ; for - {}", FACADE_LOGGER);
    }

    public void sellIngredient(String name, Integer number) {
        User user = AlchemyUtils.setRole(getAlchemyRole(), AlchemyUtils.getUserFromContextHolder());
        Ingredient ingredient = ingredientRepository.getIngredientByName(name);
        if (AlchemyUtils.ifContains(Collections.singletonList(user.getIngredients()), ingredient)) {
            Integer ingredientAmount = amountRepository
                    .findByUserIdAndIngredientId(user.getId(), ingredient.getId())
                    .getAmount();
            if (ingredientAmount > number) {
                amountRepository
                        .findByUserIdAndIngredientId(user.getId(), ingredient.getId())
                        .setAmount(ingredientAmount - number);
                if (ingredientAmount == 1)
                    if (!AlchemyUtils.ifRemoveItem(Collections.singletonList(user.getIngredients()), ingredient))
                        return;
                Long cost = AlchemyUtils.isElixir(ingredient)
                        ? ingredient.getCost()
                        : ingredient.getCost() / 2;
                user.setCoins(user.getCoins() + cost);
                userRepository.save(user);
                log.info("Ingredient {} was sold ; for - {}", name, FACADE_LOGGER);
            } else log.info("You have less ingredients than {}; for - {}", number, FACADE_LOGGER);
        } else log.info("You have no such ingredient: {}; for - {}", name, FACADE_LOGGER);
    }

    public IngredientDto makeElixir(CombineIngredientsRequest items) {
        User user = AlchemyUtils.setRole(getAlchemyRole(), AlchemyUtils.getUserFromContextHolder());
        if (isItemsMatchIngredients(items.getIngredients(), user.getIngredients())) {
            Ingredient resultElixir = ifRecipeUnlocked(items.getIngredients(), user);
            Amount amount;
            if (resultElixir != null) {
                amount = amountRepository.findByUserIdAndIngredientId(user.getId(), resultElixir.getId());
                consumeSuccessfulUsedIngredients(items.getIngredients(), user, amount);
                user.getIngredients().add(resultElixir);
                userRepository.save(user);
                log.info("User {} successfully created {} elixir with helping already unlocked recipe; for - {}",
                        user.getName(), resultElixir.getName(), FACADE_LOGGER);
                return ingredientTransformer.entityToDto(resultElixir);
            }
            List<Ingredient> elixirList = ingredientTransformer
                    .dtoToEntity(ingredientService
                            .filterBy(IngredientType.ELIXIR));
            resultElixir = combineIngredients(items.getIngredients(), elixirList);
            if (resultElixir != null) {
                amount = amountRepository.findByUserIdAndIngredientId(user.getId(), resultElixir.getId());
                user.getIngredients().add(resultElixir);
                user.getUnlockedRecipes().add(recipeRepository.getByName(resultElixir.getName()));
                consumeSuccessfulUsedIngredients(items.getIngredients(), user, amount);
                userRepository.save(user);
                log.info("User {} successfully created {} elixir; for - {}",
                        user.getName(), resultElixir.getName(), FACADE_LOGGER);
                return ingredientTransformer.entityToDto(resultElixir);
            } else {
                consumeUnsuccessfulUsedIngredients(items.getIngredients(), user.getIngredients(), user);
                userRepository.save(user);
                log.info("Unsuccessful making elixir; for - {}", FACADE_LOGGER);
                return null;
            }
        }
        log.info("User {} hasn't such ingredients; for - {}", user.getName(), FACADE_LOGGER);
        return null;
    }

    private UserRoles getAlchemyRole() {
        return rolesRepository.findByName(SIMPLE_ALCHEMIST);
    }

    private Ingredient combineIngredients(List<Ingredient> combination, List<Ingredient> elixirList) {
        return elixirList.stream()
                .map(elixir -> matchWithRecipes(elixir, combination))
                .findFirst().orElse(null);
    }

    private Ingredient matchWithRecipes(Ingredient elixir, List<Ingredient> combination) {
        List<Ingredient> matchResult = recipeRepository.getByName(elixir.getName()).getRecipes()
                .stream()
                .filter(combination::contains)
                .collect(Collectors.toList());
        if (matchResult.size() == combination.size()) return elixir;
        return null;
    }

    private Ingredient ifRecipeUnlocked(List<Ingredient> combination, User user) {
        return user.getUnlockedRecipes()
                .stream()
                .map(Recipe::getRecipes)
                .map(ingredients -> combineIngredients(combination, ingredients))
                .findFirst()
                .orElse(null);
    }

    private boolean isItemsMatchIngredients(List<Ingredient> combination, List<Ingredient> ingredients) {
        List<Ingredient> matchResult = ingredients
                .stream()
                .filter(combination::contains)
                .collect(Collectors.toList());
        return matchResult.size() == combination.size();
    }

    private void consumeSuccessfulUsedIngredients(
            List<Ingredient> combination, User user, Amount amount) {
        amount.setAmount(AlchemyUtils
                .changeAmount(amount.getAmount(), 1, true));
        amountRepository.save(amount);
        combination
                .forEach(ingredient -> amountRepository
                        .save(amountAfterConsuming(ingredient, user)));
        combination
                .forEach(c -> user.getIngredients()
                        .removeIf(i -> i.equals(c)
                                && amountRepository
                                .findByUserIdAndIngredientId(user.getId(), i.getId()).getAmount() == 0));
    }

    private void consumeUnsuccessfulUsedIngredients(
            List<Ingredient> combination, List<Ingredient> ingredients, User user) {
        combination
                .forEach(ingredient -> amountRepository
                        .save(amountAfterConsuming(
                                ingredient,
                                user,
                                AlchemyUtils.consumingProbability(ingredient.getType()))));
        combination
                .forEach(c -> ingredients
                        .removeIf(i -> i.equals(c)
                                && amountRepository
                                .findByUserIdAndIngredientId(user.getId(), i.getId()).getAmount() == 0));
    }

    private Amount amountAfterConsuming(Ingredient ingredient, User user) {
        Amount result = amountRepository.findByUserIdAndIngredientId(user.getId(), ingredient.getId());
        result.setAmount(AlchemyUtils.changeAmount(result.getAmount(), 1, false));
        return result;
    }

    private Amount amountAfterConsuming(Ingredient ingredient, User user, boolean saveIngredient) {
        Amount result = amountRepository.findByUserIdAndIngredientId(user.getId(), ingredient.getId());
        if (!saveIngredient) result.setAmount(AlchemyUtils.changeAmount(result.getAmount(), 1, false));
        return result;
    }
}
