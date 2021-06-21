package com.alchemistry.repos;

import com.alchemistry.entities.Elixir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElixirRepository extends JpaRepository<Elixir,String>  {

    Elixir getElixirByName(String name);

    List<Elixir> orderByNameAsc();

    List<Elixir> orderByLevel();

    List<Elixir> orderByCostAsc();

    List<Elixir> findByName(String name);

    List<Elixir> findByLevel(Integer level);

    List<Elixir> findByCostBefore(Long cost);

    List<Elixir> findByCostAfter(Long cost);
}
