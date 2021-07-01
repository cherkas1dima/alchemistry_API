package com.alchemistry.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "anUser", toBuilder = true)
@Entity
@Table(name = "roles")
@ToString(exclude = "users")
public class UserRoles {

    @Id
    @GenericGenerator(name = "role_id", strategy = "com.alchemistry.utils.UUIDIdGenerator")
    @GeneratedValue(generator = "role_id")
    private String id;
    @Column(name = "role_name")
    private String name;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;
}
