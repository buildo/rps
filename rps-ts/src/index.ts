import app from "./app";
import { log } from "./log";

const port =
  process.env.SERVICE_PORT !== undefined
    ? parseInt(process.env.SERVICE_PORT)
    : 8080;
const host = process.env.SERVICE_INTERFACE || "0.0.0.0";

app.listen(port, host, () => {
  log.info(`listening on ${host}:${port}`);
});
