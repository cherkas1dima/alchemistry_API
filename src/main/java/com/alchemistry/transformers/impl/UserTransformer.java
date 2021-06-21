package com.alchemistry.transformers.impl;

import com.alchemistry.dto.modelsdto.UserDto;
import com.alchemistry.entities.User;
import com.alchemistry.transformers.Transformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTransformer implements Transformer<User, UserDto> {

    @Override
    public UserDto entityToDto(User entity) {
        return UserDto.anUserDto()
                .setName(entity.getName())
                .setE_mail(entity.getE_mail())
                .setPassword(entity.getPassword())
                .setCoins(entity.getCoins())
                .setElixirs(entity.getElixirs())
                .setIngredients(entity.getIngredients())
                .build();
    }

    @Override
    public User dtoToEntity(UserDto dto) {
        return User.anUser()
                .setName(dto.getName())
                .setE_mail(dto.getE_mail())
                .setPassword(dto.getPassword())
                .setCoins(dto.getCoins())
                .setElixirs(dto.getElixirs())
                .setIngredients(dto.getIngredients())
                .build();
    }
}
