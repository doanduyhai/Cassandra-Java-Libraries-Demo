package com.datastax.demo;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(keyspace = UserEntity.KEYSPACE, name = UserEntity.USERS)
public class UserEntity {

    @Transient
    public static final String KEYSPACE = "mapper_module";

    @Transient
    public static final String USERS = "users";

    @PartitionKey
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;


}
