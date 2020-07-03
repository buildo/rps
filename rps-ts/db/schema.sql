CREATE TYPE move AS ENUM ('Rock', 'Paper', 'Scissor');
CREATE TYPE outcome AS ENUM ('Win', 'Lose', 'Draw');
CREATE TABLE play (
    id serial PRIMARY KEY,
    user_move move,
    computer_move move,
    result outcome,
    created_at timestamp default now()
);