CREATE TABLE personne
(
    id              SERIAL          NOT NULL,
    firstname       VARCHAR(50)     NOT NULL,
    lastname        VARCHAR(50)     NOT NULL,
    age             int2            NOT NULL,
    street          VARCHAR(255),
    city            VARCHAR(50),
    zipcode         int2,
    creation_date   DATE            NOT NULL DEFAULT CURRENT_DATE,
    update_date     DATE            NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT personne_id_pk PRIMARY KEY (id)
);