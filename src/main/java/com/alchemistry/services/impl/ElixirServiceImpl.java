package com.alchemistry.services.impl;

import com.alchemistry.dto.modelsdto.ElixirDto;
import com.alchemistry.entities.Elixir;
import com.alchemistry.repos.ElixirRepository;
import com.alchemistry.services.ElixirService;
import com.alchemistry.transformers.Transformer;
import com.alchemistry.utils.AlchemySortParams;
import com.alchemistry.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alchemistry.utils.AlchemistryContants.ELIXIR_SERVICE_LOGGER;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElixirServiceImpl implements ElixirService {

    private final ElixirRepository elixirRepository;
    private final Transformer<Elixir, ElixirDto> elixirTransformer;

    @Override
    public ElixirDto getById(String id) {
        ElixirDto result = elixirRepository.findById(id)
                .map(elixirTransformer::entityToDto)
                .orElseThrow(() -> new NotFoundException(ELIXIR_SERVICE_LOGGER, id));
        log.info("IN findById found - {}; for - {}", result.toString(), ELIXIR_SERVICE_LOGGER);
        return result;
    }

    @Override
    public ElixirDto getByName(String elixirName) {
        ElixirDto result = elixirTransformer.entityToDto(elixirRepository.getElixirByName(elixirName));
        log.info("IN getByName found - {}; for - {}", result.toString(), ELIXIR_SERVICE_LOGGER);
        return result;
    }

    @Override
    public List<ElixirDto> getAll() {
        List<ElixirDto> result = elixirTransformer.entityToDto(elixirRepository.findAll());
        log.info("IN getAll - {} entities found; for - {}", result.size(), ELIXIR_SERVICE_LOGGER);
        return result;
    }

    @Override
    public void delete(String id) {
        elixirRepository.deleteById(id);
        log.info("IN delete entity with id: {}; for - {}", id, ELIXIR_SERVICE_LOGGER);
    }

    @Override
    public List<ElixirDto> sortBy(AlchemySortParams parameter) {
        List<ElixirDto> result = elixirTransformer.entityToDto(sort(parameter));
        log.info("Entities were sorted by: {}; for - {}", parameter.name(), ELIXIR_SERVICE_LOGGER);
        return result;
    }

    @Override
    public List<ElixirDto> filterBy(Object parameter) {
        List<ElixirDto> result = elixirTransformer.entityToDto(filter(parameter));
        log.info("Entities were filtered by: {}; for - {}", parameter.getClass(), ELIXIR_SERVICE_LOGGER);
        return result;
    }

    @Override
    public List<ElixirDto> filterByCost(Long cost, Boolean moreThanCost) {
        List<Elixir> list;
        if (moreThanCost) list = elixirRepository.findByCostAfter(cost);
        else list = elixirRepository.findByCostBefore(cost);
        List<ElixirDto> result = elixirTransformer.entityToDto(list);
        log.info("Entities were filtered by cost; for - {}", ELIXIR_SERVICE_LOGGER);
        return result;
    }

    private List<Elixir> sort(AlchemySortParams parameter) {
        if (parameter.equals(AlchemySortParams.NAME)) return elixirRepository.orderByNameAsc();
        if (parameter.equals(AlchemySortParams.LEVEL)) return elixirRepository.orderByLevel();
        if (parameter.equals(AlchemySortParams.COST)) return elixirRepository.orderByCostAsc();
        throw new RuntimeException("Wrong parameter sort type");
    }

    private List<Elixir> filter(Object parameter) {
        if (parameter instanceof String) return elixirRepository.findByName((String) parameter);
        if (parameter instanceof Integer) return elixirRepository.findByLevel((Integer) parameter);
        throw new RuntimeException("Wrong parameter filter type");
    }
}
