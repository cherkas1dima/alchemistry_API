package com.alchemistry.utils;

public interface AlchemistryContants {

    String USER_SERVICE_LOGGER = "UserService_logger";
    String INGREDIENT_SERVICE_LOGGER = "IngredientService_logger";
    String ELIXIR_SERVICE_LOGGER = "ElixirService_logger";
    String JWT_SERVICE_LOGGER = "JwtUserDetailsService_logger";
    String FACADE_LOGGER = "AlchemyFacade_logger";

    Long SIMPLE_ALCHEMIST_STARTER_COINS_PACK = 1000L;

    String UNIMPLEMENTED = "Unimplemented method";

    String MAGISTER_ALCHEMIST = "Admin";
    String SIMPLE_ALCHEMIST = "User";

    String LOGIN_ALCHEMIST_ENDPOINT = "/api/v1/auth/login";

    String MESSAGE_NOT_FOUND = "%s with %s='%s' was not found";
}
