CREATE KEYSPACE IF NOT EXISTS cassandra_unit
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS cassandra_unit.users(
    login text PRIMARY KEY,
    name text,
    age int);