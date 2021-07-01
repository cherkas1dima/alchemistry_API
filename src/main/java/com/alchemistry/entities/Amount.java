package com.alchemistry.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "anAmount", toBuilder = true)
@Entity
@Table(name = "ingredient_amount")
@IdClass(Amount.CompositeId.class)
public class Amount {

    @Id
    @Column(name = "user_id")
    private String userId;
    @Id
    @Column(name = "ingredient_id")
    private String ingredientId;
    @Column(name = "amount")
    private Integer amount;

    @NoArgsConstructor
    @Data
    @Builder
    @EqualsAndHashCode
    public static class CompositeId implements Serializable {

        private String user_id;
        private String ingredient_id;
    }

}
