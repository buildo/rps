import pgPromise, { ParameterizedQuery as PQ } from "pg-promise";

const cn = {
  host: "localhost",
  port: 5438,
  database: "root",
  user: "root",
  password: "root",
  max: 30, // use up to 30 connections
};

const pgp = pgPromise();
const db = pgp(cn);

export async function lastGame() {
  return await db.oneOrNone(
    "SELECT game_date, result FROM results ORDER BY game_date DESC LIMIT 1"
  );
}

export async function logRes(res: string) {
  const addResult = new PQ("INSERT into results(result) VALUES($1)");
  return await db.none(addResult, [res]);
}

export async function allGames(): Promise<any[]> {
  return await db.any(
    "SELECT game_date, result FROM results ORDER BY game_date"
  );
}

export function closeDB() {
  db.$pool.end();
}
