CREATE TABLE users (
    id bigserial primary key,
    email varchar(320) not null,
    password varchar(128) not null,
    enabled boolean not null,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);