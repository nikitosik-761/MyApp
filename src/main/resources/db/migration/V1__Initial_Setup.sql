CREATE TABLE IF NOT EXISTS Customer(
                                       id BIGSERIAL PRIMARY KEY ,
                                       name TEXT NOT NULL,
                                       password TEXT NOT NULL,
                                       email TEXT NOT NULL UNIQUE,
                                       age INT NOT NULL
);
