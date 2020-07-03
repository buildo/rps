import * as express from "express";

export const healthRouter = express.Router();

healthRouter.get("/health", (_, res) => {
  res.send("OK");
});
