package com.alchemistry.transformers.impl;

import com.alchemistry.dto.modelsdto.UserDto;
import com.alchemistry.dto.requsestdto.RegistrationRequest;
import com.alchemistry.transformers.Transformer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.alchemistry.utils.AlchemistryContants.SIMPLE_ALCHEMIST_STARTER_COINS_PACK;
import static com.alchemistry.utils.AlchemistryContants.UNIMPLEMENTED;

@Component
@RequiredArgsConstructor
public class RegistrationTransformer implements Transformer<RegistrationRequest, UserDto> {

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto entityToDto(RegistrationRequest entity) {
        return UserDto.anUserDto()
                .setName(entity.getName())
                .setE_mail(entity.getE_mail())
                .setPassword(passwordEncoder.encode(entity.getPassword()))
                .setCoins(SIMPLE_ALCHEMIST_STARTER_COINS_PACK)
                .setElixirs(null)
                .setIngredients(null)
                .build();
    }

    @Override
    public RegistrationRequest dtoToEntity(UserDto dto) {
        throw new RuntimeException(UNIMPLEMENTED);
    }
}
