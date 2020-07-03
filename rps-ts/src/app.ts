import * as express from "express";
import { healthRouter } from "./routers";
import { createGameRouter } from "./routers";
import { connect } from "./repository";
import { createGameRepo } from "./repository";
import { createGameService } from "./services";

const app = express();

const db = connect();
const gameRepo = createGameRepo(db);
const gameService = createGameService(gameRepo);
const gameRouter = createGameRouter(gameService);

app.use("/", healthRouter);
app.use("/", gameRouter);

export default app;
