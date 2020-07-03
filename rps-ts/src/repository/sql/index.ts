import { QueryFile, IQueryFileOptions } from "pg-promise";

const path = require("path");

function sql(file: string): QueryFile {
  const fullPath: string = path.join(__dirname, file); // generating full path;

  const options: IQueryFileOptions = {
    minify: true,
  };

  const query: QueryFile = new QueryFile(fullPath, options);

  if (query.error) {
    console.error(query.error);
  }

  return query;
}

export const getLastGame = sql("getLastGame.sql");
export const insertGame = sql("insertGame.sql");
