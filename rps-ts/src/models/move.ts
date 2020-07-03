import * as t from "io-ts";

export const Move = t.keyof(
  {
    Rock: null,
    Paper: null,
    Scissor: null,
  },
  "Move"
);

export type Move = t.TypeOf<typeof Move>;
