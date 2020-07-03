import * as express from "express";
import { Move } from "../models";
import { pipe } from "fp-ts/lib/pipeable";
import { fold } from "fp-ts/lib/Either";
import { Errors } from "io-ts";
import { GameService } from "../services";
import * as bodyparser from "body-parser";

export const sendDecodingError = (
  errors: Errors,
  res: express.Response
): void => {
  res.status(422).send(errors);
};

export const createGameRouter = (gameService: GameService) => {
  const gameRouter = express.Router();

  gameRouter.get("/play", (_, res) => {
    gameService
      .lastPlay()()
      .then((r) => res.send(r)); //TODO handle error code return on loft
  });

  gameRouter.post("/play", bodyparser.json(), (req, res) => {
    return pipe(
      Move.decode(req.body.move),
      fold(
        (errors) => sendDecodingError(errors, res),
        (ok) =>
          gameService
            .play(ok)()
            .then((r) => res.send(r)) //TODO handle error code return on loft
      )
    );
  });

  return gameRouter;
};
