CREATE TYPE moves AS ENUM (
  'Rock',
  'Paper',
  'Scissors'
);

CREATE TYPE match_result AS ENUM (
  'PlayerWin',
  'CPUWin',
  'Draw'
);

create cast (varchar as match_result) with inout as assignment;
create cast (varchar as moves) with inout as assignment;

CREATE TABLE history (
  id uuid PRIMARY KEY,
  cpu_move moves NOT NULL,
  player_move moves NOT NULL,  
  result match_result NOT NULL,
  match_date timestamptz NOT NULL
);