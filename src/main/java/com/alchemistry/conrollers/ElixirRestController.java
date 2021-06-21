package com.alchemistry.conrollers;

import com.alchemistry.dto.modelsdto.ElixirDto;
import com.alchemistry.dto.requsestdto.ElixirFilterRequest;
import com.alchemistry.services.ElixirService;
import com.alchemistry.utils.AlchemySortParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/alchemy/api/v1/elixir/")
public class ElixirRestController {

    private final ElixirService elixirService;

    @Autowired
    public ElixirRestController(ElixirService elixirService) {
        this.elixirService = elixirService;
    }

    @GetMapping("list")
    public ResponseEntity<List<ElixirDto>> getAll() {
        try {
            return ResponseEntity.ok(elixirService.getAll());
        } catch (Exception e) {
            throw new RuntimeException("Getting elixir List ERROR");
        }
    }

    @GetMapping("id/{elixir_id}")
    public ResponseEntity<ElixirDto> getById(@PathVariable(value = "elixir_id") String id) {
        try {
            return ResponseEntity.ok(elixirService.getById(id));
        } catch (Exception e) {
            throw new RuntimeException("Getting elixir by id ERROR");
        }
    }

    @GetMapping("name/{elixir_name}")
    public ResponseEntity<ElixirDto> getByName(@PathVariable(value = "elixir_name") String name) {
        try {
            return ResponseEntity.ok(elixirService.getByName(name));
        } catch (Exception e) {
            throw new RuntimeException("Getting elixir by name ERROR");
        }
    }

    @GetMapping("sort/{parameter}")
    public ResponseEntity<List<ElixirDto>> sortBy(@PathVariable(value = "parameter") AlchemySortParams parameter) {
        try {
            if (parameter == null) parameter = AlchemySortParams.NAME;
            return ResponseEntity.ok(elixirService.sortBy(parameter));
        } catch (Exception e) {
            throw new RuntimeException("Sorting elixirs by parameter: " + parameter + "; ERROR");
        }
    }

    @PostMapping("filter")
    public ResponseEntity<List<ElixirDto>> filterBy(
            @RequestBody ElixirFilterRequest parameter) {
        try {
            List<ElixirDto> result;
            if (parameter.getName() != null) result = elixirService.filterBy(parameter.getName());
            else if (parameter.getLevel() != null) result = elixirService.filterBy(parameter.getLevel());
            else if (parameter.getCost() != null && parameter.getMoreThanCost() != null)
                result = elixirService.filterByCost(parameter.getCost(), parameter.getMoreThanCost());
            else throw new RuntimeException("Filtering elixirs by parameter: " + parameter + "; ERROR");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException("Filtering elixirs by parameter: " + parameter + "; ERROR");
        }
    }
}
