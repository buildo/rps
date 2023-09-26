import { zodiosApp } from "@zodios/express";
import { makeApi } from "@zodios/core";
import { z } from "zod";
import { read } from "../model/Move";
import {
  welcome,
  generateComputerMove,
  playLogic,
  allGamesParsed,
} from "../game";
import { ResultArray, result } from "../model/Result";

export const apis = makeApi([
  {
    method: "get",
    path: "/welcome",
    description: "Get game instructions",
    response: z.object({
      instructions: z.array(z.string()),
    }),
  },
  {
    method: "get",
    path: "/allgames",
    description: "Get all the past games result and timestamps",
    response: z.object({
      game_history: z.array(result),
    }),
  },
  {
    method: "post",
    path: "/play/:move",
    description: "Play using your <move>",
    parameters: [
      {
        name: "move",
        type: "Path",
        schema: z.string(),
      },
    ],
    response: z.object({
      result: z.string(),
    }),
  },
]);

// app is just an express app with type zodios validations
export const app = zodiosApp(apis);

app
  .get("/welcome", async (_, res) => {
    const welcomeMessage = await welcome();
    return res
      .status(200)
      .json({
        instructions: welcomeMessage,
      })
      .end();
  })
  .get("/allgames", async (_, res) => {
    try {
      const allResults = await allGamesParsed();
      return res
        .status(200)
        .json({
          game_history: allResults,
        })
        .end();
    } catch (error) {
      return res
        .status(500)
        .json({
          game_history: error,
        })
        .end();
    }
  })
  .post("/play/:move", async (req, res) => {
    const userMove = read(req.params.move);
    const computerMove = generateComputerMove();
    const outcome = await playLogic(userMove, computerMove);
    return res
      .status(200)
      .json({
        result: outcome,
      })
      .end();
  });
