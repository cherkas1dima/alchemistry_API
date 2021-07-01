package com.alchemistry.services.impl;

import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.IngredientType;
import com.alchemistry.repos.IngredientRepository;
import com.alchemistry.services.IngredientService;
import com.alchemistry.transformers.impl.IngredientTransformer;
import com.alchemistry.utils.AlchemySortParams;
import com.alchemistry.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alchemistry.utils.AlchemistryContants.INGREDIENT_SERVICE_LOGGER;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientTransformer ingredientTransformer;
    private final IngredientRepository ingredientRepository;

    @Override
    public IngredientDto getById(String id) {
        IngredientDto result = ingredientRepository.findById(id)
                .map(ingredientTransformer::entityToDto)
                .orElseThrow(() -> new NotFoundException(INGREDIENT_SERVICE_LOGGER, id));
        log.info("IN findById found - {}; for - {}", result.toString(), INGREDIENT_SERVICE_LOGGER);
        return result;
    }

    @Override
    public IngredientDto getByName(String ingredientName) {
        IngredientDto result =
                ingredientTransformer.entityToDto(ingredientRepository.getIngredientByName(ingredientName));
        log.info("IN getByName found - {}; for - {}", result.toString(), INGREDIENT_SERVICE_LOGGER);
        return result;
    }

    @Override
    public List<IngredientDto> getAll() {
        List<IngredientDto> result = ingredientTransformer.entityToDto(ingredientRepository.findAll());
        log.info("IN getAll - {} entities found; for - {}", result.size(), INGREDIENT_SERVICE_LOGGER);
        return result;
    }

    @Override
    public void delete(String id) {
        ingredientRepository.deleteById(id);
        log.info("IN delete entity with id: {}; for - {}", id, INGREDIENT_SERVICE_LOGGER);
    }

    @Override
    public List<IngredientDto> sortBy(AlchemySortParams parameter) {
        List<IngredientDto> result = ingredientTransformer.entityToDto(sort(parameter));
        log.info("Entities were sorted by: {}; for - {}", parameter.name(), INGREDIENT_SERVICE_LOGGER);
        return result;
    }

    @Override
    public List<IngredientDto> filterBy(Object parameter) {
        List<IngredientDto> result = ingredientTransformer.entityToDto(filter(parameter));
        log.info("Entities were filtered by: {}; for - {}", parameter.getClass(), INGREDIENT_SERVICE_LOGGER);
        return result;
    }

    @Override
    public List<IngredientDto> filterByCost(Long cost, Boolean moreThanCost) {
        List<Ingredient> list;
        if (moreThanCost) list = ingredientRepository.findByCostAfter(cost);
        else list = ingredientRepository.findByCostBefore(cost);
        List<IngredientDto> result = ingredientTransformer.entityToDto(list);
        log.info("Entities were filtered by cost; for - {}", INGREDIENT_SERVICE_LOGGER);
        return result;
    }

    private List<Ingredient> sort(AlchemySortParams parameter) {
        if (parameter.equals(AlchemySortParams.NAME)) return ingredientRepository.orderByNameAsc();
        if (parameter.equals(AlchemySortParams.TYPE)) return ingredientRepository.orderByTypeAsc();
        if (parameter.equals(AlchemySortParams.COST)) return ingredientRepository.orderByCostAsc();
        if (parameter.equals(AlchemySortParams.LEVEL)) return ingredientRepository.orderByLevel();
        throw new RuntimeException("Wrong parameter sort type");
    }

    private List<Ingredient> filter(Object parameter) {
        if (parameter instanceof String) return ingredientRepository.findByName((String) parameter);
        if (parameter instanceof IngredientType) return ingredientRepository.findByType((IngredientType) parameter);
        if (parameter instanceof Integer) return ingredientRepository.findByLevel((Integer) parameter);
        throw new RuntimeException("Wrong parameter filter type");
    }
}
