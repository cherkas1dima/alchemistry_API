package com.alchemistry.services.impl;

import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.dto.modelsdto.UserDto;
import com.alchemistry.dto.requsestdto.RegistrationRequest;
import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.User;
import com.alchemistry.entities.UserRoles;
import com.alchemistry.repos.UserRepository;
import com.alchemistry.repos.UserRolesRepository;
import com.alchemistry.services.UserService;
import com.alchemistry.transformers.Transformer;
import com.alchemistry.utils.AlchemyUtils;
import com.alchemistry.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alchemistry.utils.AlchemistryContants.SIMPLE_ALCHEMIST;
import static com.alchemistry.utils.AlchemistryContants.USER_SERVICE_LOGGER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Transformer<User, UserDto> userTransformer;
    private final Transformer<Ingredient, IngredientDto> ingredientTransformer;
    private final Transformer<RegistrationRequest, UserDto> regTransformer;
    private final UserRepository userRepository;
    private final UserRolesRepository rolesRepository;

    @Override
    public Transformer<User, UserDto> getUserTransformer() {
        return userTransformer;
    }

    @Override
    public UserDto getById(String id) {
        UserDto result = userRepository.findById(id)
                .map(userTransformer::entityToDto)
                .orElseThrow(() -> new NotFoundException(USER_SERVICE_LOGGER, "id", id));
        log.info("IN findById found - {}; for - {}", result.toString(), USER_SERVICE_LOGGER);
        return result;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> result = userTransformer.entityToDto(userRepository.findAll());
        log.info("IN getAll - {} entities found; for - {}", result.size(), USER_SERVICE_LOGGER);
        return result;
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
        log.info("IN delete entity with id: {}; for - {}", id, USER_SERVICE_LOGGER);
    }

    @Override
    public UserDto getByName(String userName) {
        UserDto result = userRepository.getUserByName(userName)
                .map(userTransformer::entityToDto)
                .orElseThrow(() -> new NotFoundException(USER_SERVICE_LOGGER, "name", userName));
        log.info("IN getByName found - {}; for - {}", result.toString(), USER_SERVICE_LOGGER);
        return result;
    }

    @Override
    public void register(RegistrationRequest reqDto) {
        User user =
                userTransformer.dtoToEntity(
                        regTransformer.entityToDto(reqDto)
                );
        UserRoles role = rolesRepository.findByName(SIMPLE_ALCHEMIST);
        userRepository.saveAndFlush(AlchemyUtils.setRole(role, user));
        log.info("IN registered entity: {}; for - {}", reqDto, USER_SERVICE_LOGGER);
    }
}
