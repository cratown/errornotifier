CREATE TABLE project_errors (
    id bigserial primary key,
    project_name varchar(128),
    body text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);

CREATE INDEX project_name_idx ON project_errors (project_name);