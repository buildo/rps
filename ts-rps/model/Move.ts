import { z } from "zod";

const moves = {
  0: "Rock",
  1: "Paper",
  2: "Scissors",
} as const;

export const Move = z.enum(["0", "1", "2"]).transform((index) => moves[index]);
export type Move = z.infer<typeof Move>;

export function read(input: string): Move {
  const res = Move.safeParse(input);
  if (res.success) {
    return res.data;
  } else {
    throw new RangeError(
      "Sorry, you must enter a valid move (0, 1 or 2). Try again"
    );
  }
}
