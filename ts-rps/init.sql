create table Results (
    id SERIAL PRIMARY KEY,
    result varchar NOT NULL,
    game_date timestamp NOT NULL DEFAULT now()
);