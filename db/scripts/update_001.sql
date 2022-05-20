create table post
(
    id          serial primary key,
    name        text,
    description text,
    city_id     int,
    created     timestamp,
    visible     boolean default false
);

create table candidate
(
    id          serial primary key,
    name        text,
    photo       bytea,
    visible     boolean default false,
    description text,
    created     timestamp
);

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     varchar(250),
    email    varchar(250),
    password TEXT,
    created  timestamp
);

ALTER TABLE users
    ADD CONSTRAINT email_unique UNIQUE (email);
