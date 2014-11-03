package com.datastax.demo;

import com.datastax.driver.core.*;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CassandraUnitTest {

    private static final String KEYSPACE = "cassandra_unit";
    private static final String USERS = "users";

    @Rule
    public CassandraCQLUnit embeddedCassandra = new CassandraCQLUnit(new ClassPathCQLDataSet("schema.cql"));
//    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("schema.cql"),"configurationFileName", "localhost", 9240);

    private Session session;

    private PreparedStatement insert;
    private PreparedStatement select;

    @Before
    public void setUp() {
        session = embeddedCassandra.session;
        insert = session.prepare(insertInto(KEYSPACE, USERS)
                .value("login", bindMarker())
                .value("name", bindMarker())
                .value("age", bindMarker()));

        select = session.prepare(select().from(KEYSPACE, USERS).where(eq("login", bindMarker())));
    }

    @Test
    public void should_insert_data() throws Exception {
        //Given
        session.execute(insert.bind("jdoe", "John DOE", 33));

        //When
        final Row row = session.execute(select.bind("jdoe")).one();

        //Then
        assertThat(row).isNotNull();
        assertThat(row.getString("login")).isEqualTo("jdoe");
        assertThat(row.getString("name")).isEqualTo("John DOE");
        assertThat(row.getInt("age")).isEqualTo(33);

    }
}
