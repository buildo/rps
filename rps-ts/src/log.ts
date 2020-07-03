import * as _debug from "debug";
import * as t from "io-ts";
import { pipe } from "fp-ts/lib/pipeable";
import { fold } from "fp-ts/lib/Either";

const LogLevel = t.keyof(
  {
    DEBUG: true,
    INFO: true,
    WARN: true,
    ERROR: true,
  },
  "LogLevel"
);
type LogLevel = t.TypeOf<typeof LogLevel>;

const logLevel = pipe(
  LogLevel.decode(process.env.DAVINCI_LOG_LEVEL),
  fold(() => "INFO" as LogLevel, t.identity)
);

const namespace = (level: LogLevel) => `IS-${level}`;

const enabledLevelsByLogLevel: { [k in LogLevel]: LogLevel[] } = {
  DEBUG: ["ERROR", "WARN", "INFO", "DEBUG"],
  INFO: ["ERROR", "WARN", "INFO"],
  WARN: ["ERROR", "WARN"],
  ERROR: ["ERROR"],
};

const enabledNameSpaces = enabledLevelsByLogLevel[logLevel].map(namespace);

_debug.enable(enabledNameSpaces.join(","));

function _log(level: LogLevel) {
  return _debug(namespace(level));
}

export const log = {
  debug: _log("DEBUG"),
  info: _log("INFO"),
  warn: _log("WARN"),
  error: _log("ERROR"),
};
