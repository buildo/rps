import { IDatabase } from "pg-promise";
import { IClient } from "pg-promise/typescript/pg-subset";
import { PlayResponse } from "../models";
import { TaskEither, tryCatch } from "fp-ts/lib/TaskEither";
import * as sql from "./sql";

export interface GameRepository {
  getLast: TaskEither<unknown, PlayResponse>;
  insert: (p: PlayResponse) => TaskEither<unknown, void>;
}

export const createGameRepo = (db: IDatabase<{}, IClient>): GameRepository => ({
  getLast: tryCatch(
    () => db.one<PlayResponse>(sql.getLastGame),
    (error) => error //TODO: decode errors
  ),
  insert: (play: PlayResponse) =>
    tryCatch(
      () => db.none(sql.insertGame, play).then(() => undefined),
      (error) => error //TODO: decode errors
    ),
});
