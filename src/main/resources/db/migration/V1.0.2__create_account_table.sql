CREATE TABLE account
(
    id              SERIAL          NOT NULL,
    id_person       SERIAL          NOT NULL,
    login           VARCHAR(50)     NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    creation_date   DATE            NOT NULL DEFAULT CURRENT_DATE,
    update_date     DATE            NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT account_id_pk PRIMARY KEY (id)
);

alter table account
    add constraint fk_person foreign key (id_person) references personne (id)