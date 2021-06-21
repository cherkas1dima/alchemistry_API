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
@Builder(setterPrefix = "set", builderMethodName = "anElixir", toBuilder = true)
@Entity
@Table(name = "elixir")
public class Elixir {

    @Id
    @GenericGenerator(name = "elixir_id", strategy = "com.alchemistry.utils.UUIDIdGenerator")
    @GeneratedValue(generator = "elixir_id")
    private String id;
    @Column(name = "elixir_name")
    private String name;
    @Column(name = "cost")
    private Long cost;
    @Column(name = "level")
    private Integer level;
    @ManyToMany(mappedBy="elixirs", fetch = FetchType.EAGER)
    private List<Ingredient> recipe;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> elixirOwners;
}
