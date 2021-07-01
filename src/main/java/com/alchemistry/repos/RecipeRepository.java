package com.alchemistry.repos;

import com.alchemistry.entities.Recipe;
import com.alchemistry.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe,String> {

    List<Recipe> findByOwners(User owner);

    Recipe getByName(String name);
}
