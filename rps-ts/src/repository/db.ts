import * as pgPromise from "pg-promise";
import pg = require("pg-promise/typescript/pg-subset");

export const pgp = pgPromise({
  error: (e) => console.error("Database Error", e),
});

export const connect = (): pgPromise.IDatabase<{}, pg.IClient> => {
  const cn = {
    host: "localhost",
    port: 9090,
    database: "rps",
    user: "postgres",
    password: "rps",
    max: 2,
  };

  const db = pgp(cn);
  return db;
};
