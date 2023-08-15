-- Drop Table
-- DROP TABLE IF EXISTS customers;

-- Table
CREATE TABLE IF NOT EXISTS customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255));

-- Function add(1,1)
CREATE OR REPLACE FUNCTION add(a integer, b integer) RETURNS integer
    LANGUAGE SQL
    IMMUTABLE
    RETURNS NULL ON NULL INPUT
    RETURN a + b;

-- Function findLastNameByLength
CREATE OR REPLACE FUNCTION get_records_by_last_name_length(input_count integer)
RETURNS TABLE (id integer, first_name varchar, last_name varchar) AS $$
BEGIN
    RETURN QUERY
    SELECT c.id, c.first_name, c.last_name
    FROM customers AS c
    WHERE LENGTH(c.last_name) = input_count;
END;
$$ LANGUAGE plpgsql;
