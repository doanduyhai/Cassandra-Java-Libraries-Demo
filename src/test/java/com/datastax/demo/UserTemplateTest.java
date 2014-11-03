package com.datastax.demo;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import org.assertj.core.api.Assertions;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.datastax.demo.UserEntity.KEYSPACE;
import static com.datastax.demo.UserEntity.USERS;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTemplateTest {
    @Rule
    public CassandraCQLUnit embeddedCassandra = new CassandraCQLUnit(new ClassPathCQLDataSet("mapper_module.cql"));

    private Session session;
    private UserTemplate template;
    
    @Before
    public void setUp() {
        session = embeddedCassandra.session;
        template = new MappingManager(session).createAccessor(UserTemplate.class);
        session.execute("TRUNCATE mapper_module.users");
    }
    
    @Test
    public void should_find_users_using_template() throws Exception {
        //Given
        session.execute(insertInto(KEYSPACE, USERS)
                .value("login", "jdoe")
                .value("name", "John DOE")
                .value("age", 33));

        session.execute(insertInto(KEYSPACE, USERS)
                .value("login", "hsue")
                .value("name", "Helen SUE")
                .value("age", 27));

        session.execute(insertInto(KEYSPACE, USERS)
                .value("login", "rsmith")
                .value("name", "Richard SMITH")
                .value("age", 47));

        //When
        final List<UserEntity> users = template.findNUsers(2).all();

        //Then
        assertThat(users).hasSize(2);
    }
}
