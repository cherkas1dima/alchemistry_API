package com.alchemistry.services;

import com.alchemistry.dto.modelsdto.IngredientDto;
import com.alchemistry.dto.modelsdto.UserDto;
import com.alchemistry.dto.requsestdto.RegistrationRequest;
import com.alchemistry.entities.User;
import com.alchemistry.transformers.Transformer;

import java.util.List;

public interface UserService {

    UserDto getById(String id);

    UserDto getByName(String name);

    List<UserDto> getAll();

    void delete(String id);

    void register(RegistrationRequest userDto);

    Transformer<User,UserDto> getUserTransformer();

    List<List<IngredientDto>> getAvailableRecipes();
}
