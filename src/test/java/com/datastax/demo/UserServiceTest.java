package com.datastax.demo;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.datastax.demo.UserEntity.KEYSPACE;
import static com.datastax.demo.UserEntity.USERS;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest {

    @Rule
    public CassandraCQLUnit embeddedCassandra = new CassandraCQLUnit(new ClassPathCQLDataSet("mapper_module.cql"));

    private Session session;
    private UserService service = new UserService();

    @Before
    public void setUp() {
        session = embeddedCassandra.session;
        service.mapper = new MappingManager(session).mapper(UserEntity.class);
        session.execute("TRUNCATE mapper_module.users");
    }
    
    @Test
    public void should_create_user() throws Exception {
        //Given
        service.create("jdoe","John DOE",33);

        //When
        final Row row = session.execute(select().from(KEYSPACE, USERS).where(eq("login", "jdoe"))).one();

        //Then
        assertThat(row).isNotNull();
    }

    @Test
    public void should_find_user() throws Exception {
        //Given
        session.execute(insertInto(KEYSPACE, USERS)
                .value("login", "jdoe")
                .value("name", "John DOE")
                .value("age", 33));

        //When
        final UserEntity found = service.find("jdoe");

        //Then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("John DOE");
        assertThat(found.getAge()).isEqualTo(33);
    }

    @Test
    public void should_delete_user() throws Exception {
        //Given
        session.execute(insertInto(KEYSPACE, USERS)
                .value("login", "jdoe")
                .value("name", "John DOE")
                .value("age", 33));


        //When
        service.delete("jdoe");

        //Then
        final Row row = session.execute(select().from(KEYSPACE, USERS).where(eq("login", "jdoe"))).one();

        assertThat(row).isNull();
    }
}