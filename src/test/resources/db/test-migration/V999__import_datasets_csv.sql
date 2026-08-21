COPY personne(firstname, lastname, age, street, city, zipcode)
FROM '/tmp/insert_personnes.csv'
WITH (FORMAT csv, HEADER true, DELIMITER ',');
COPY account(id_person, login, password)
    FROM '/tmp/insert_accounts.csv'
    WITH (FORMAT csv, HEADER true, DELIMITER ',');