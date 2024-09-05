CREATE TABLE verification_tokens (
    id bigserial primary key,
    user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token varchar(128),
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);

CREATE INDEX verification_token_token_idx ON verification_tokens(token);