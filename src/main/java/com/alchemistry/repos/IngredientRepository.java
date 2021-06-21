package com.alchemistry.repos;

import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.IngredientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,String>  {

    Ingredient getIngredientByName(String name);

    List<Ingredient> orderByNameAsc();

    List<Ingredient> orderByCostAsc();

    List<Ingredient> orderByTypeAsc();

    List<Ingredient> findByName(String name);

    List<Ingredient> findByCostBefore(Long cost);

    List<Ingredient> findByCostAfter(Long cost);

    List<Ingredient> findByType(IngredientType type);
}
