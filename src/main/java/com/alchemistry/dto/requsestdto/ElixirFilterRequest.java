package com.alchemistry.dto.requsestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElixirFilterRequest {

    private String name;
    private Long level;
    private Long cost;
    private Boolean moreThanCost;
}
