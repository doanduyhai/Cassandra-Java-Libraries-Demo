package com.datastax.demo;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface UserTemplate {

    @Query("SELECT * FROM "+UserEntity.KEYSPACE+"."+UserEntity.USERS+" LIMIT :lim")
    public Result<UserEntity> findNUsers(@Param("lim") int limit);
}
