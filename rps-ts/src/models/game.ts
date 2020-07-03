import { Move } from "./move";
import { Result } from "./result";

export interface PlayResponse {
  userMove: Move;
  computerMove: Move;
  result: Result;
}
