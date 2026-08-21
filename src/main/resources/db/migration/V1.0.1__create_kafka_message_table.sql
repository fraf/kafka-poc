CREATE TABLE kafka_message (
       id SERIAL PRIMARY KEY NOT NULL,
       message JSON,
       creation_date DATE NOT NULL DEFAULT CURRENT_DATE
);