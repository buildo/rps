import { match, P } from "ts-pattern";

export type Move = "Rock" | "Paper" | "Scissors";

export function read(input: string): Move {
  return match(input)
    .returnType<Move>()
    .with("0", () => "Rock")
    .with("1", () => "Paper")
    .with("2", () => "Scissors")
    .otherwise(() => {
      throw new RangeError(
        "Sorry, you must enter a valid move (0, 1 or 2). Try again"
      );
    });
}
