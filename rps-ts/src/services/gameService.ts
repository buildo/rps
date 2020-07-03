import { Move, Error, Result, PlayResponse } from "../models";
import { TaskEither, right } from "fp-ts/lib/TaskEither";
import { GameRepository } from "../repository";

export interface GameService {
  play: (move: Move) => TaskEither<Error, Result>;
  lastPlay: () => TaskEither<unknown, PlayResponse>;
}

const randomMove = (): Move => {
  const random = Math.floor(Math.random() * 3) + 1;
  switch (random) {
    case 1:
      return "Rock";
    case 2:
      return "Paper";
    default:
      return "Scissor";
  }
};

export const createGameService = (repo: GameRepository): GameService => ({
  play: (move: Move): TaskEither<Error, Result> => {
    const computerMove = randomMove();
    if (move === computerMove) return right("Draw");
    switch (move) {
      case "Paper":
        return right(computerMove === "Rock" ? "Win" : "Lose");
      case "Rock":
        return right(computerMove === "Scissor" ? "Win" : "Lose");
      case "Scissor":
        return right(computerMove === "Paper" ? "Win" : "Lose");
    }
  },
  lastPlay: () => repo.getLast,
});
