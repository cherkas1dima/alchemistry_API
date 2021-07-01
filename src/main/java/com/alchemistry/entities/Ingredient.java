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
@Builder(setterPrefix = "set", builderMethodName = "anIngredient", toBuilder = true)
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GenericGenerator(name = "ingredient_id", strategy = "com.alchemistry.utils.UUIDIdGenerator")
    @GeneratedValue(generator = "ingredient_id")
    private String id;
    @Column(name = "ingredient_name")
    private String name;
    @Column(name = "ingredient_type")
    private IngredientType type;
    @Column(name = "level")
    private Integer level;
    @Column(name = "ingredient_cost")
    private Long cost;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> ingredientOwners;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Recipe> recipes;
}
