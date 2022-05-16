create table post
(
    id   serial primary key,
    name text,
    description text,
    city_id int,
    created timestamp,
    visible boolean default false
);

create table candidate
 (
     id   serial primary key,
     name text,
     photo bytea,
     visible boolean default false,
     description text,
     created timestamp
);
