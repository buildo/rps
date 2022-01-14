CREATE TYPE move AS ENUM (
  'Rock',
  'Paper',
  'Scissors'
);

CREATE TYPE match_result AS ENUM (
  'Win',
  'Lose',
  'Draw'
);

CREATE CAST (varchar AS move) WITH INOUT AS ASSIGNMENT;
CREATE CAST (varchar AS match_result) WITH INOUT AS ASSIGNMENT;

CREATE TABLE history (
  id uuid PRIMARY KEY,
  computer_move move NOT NULL,
  user_move move NOT NULL,
  result match_result NOT NULL,
  occurred_at timestamptz NOT NULL
);
