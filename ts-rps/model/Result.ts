import { z } from "zod";

export const result = z.object({
  game_date: z.coerce.date(),
  result: z.enum(["WIN", "LOSE", "DRAW"]),
});

export type Result = z.infer<typeof result>;
export const resultArray = z.array(result);
export type ResultArray = z.infer<typeof resultArray>;
