create table post
(
    id   serial primary key,
    name text,
    description text,
    city_id int,
    created timestamp,
    visible boolean default false
);
