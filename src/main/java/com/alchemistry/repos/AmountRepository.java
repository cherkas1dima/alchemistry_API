package com.alchemistry.repos;

import com.alchemistry.entities.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmountRepository extends JpaRepository<Amount,String> {

    List<Amount> findByUserId(String id);

    List<Amount> findByIngredientId(String id);

    Amount findByUserIdAndIngredientId(String userId, String ingredientId);
}
