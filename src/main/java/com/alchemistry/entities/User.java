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
@Builder(setterPrefix = "set", builderMethodName = "anUser", toBuilder = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GenericGenerator(name = "user_id", strategy = "com.alchemistry.utils.UUIDIdGenerator")
    @GeneratedValue(generator = "user_id")
    private String id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "e_mail")
    private String e_mail;
    @Column(name = "password")
    private String password;
    @Column(name = "coins")
    private Long coins;
    @ManyToMany(mappedBy="ingredientOwners", fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserRoles> roles;
    @ManyToMany(mappedBy="owners", fetch = FetchType.EAGER)
    private List<Recipe> unlockedRecipes;
}
