package com.alchemistry.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "anRecipe", toBuilder = true)
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GenericGenerator(name = "recipe_id", strategy = "com.alchemistry.utils.UUIDIdGenerator")
    @GeneratedValue(generator = "recipe_id")
    private String id;
    @Column(name = "elixir_name")
    private String name;
    @ManyToMany(mappedBy="recipes", fetch = FetchType.EAGER)
    private List<Ingredient> recipes;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> owners;
}
