import { match, P } from "ts-pattern";
import { read, Move } from "./model/Move";
import { ResultArray, resultArray, result } from "./model/Result";
import { logRes, allGames, lastGame } from "./db/db";

export function generateComputerMove(): Move {
  return read(String(Math.round(Math.random() * 2)));
}
export async function playLogic(
  userMove: Move,
  computerMove: Move
): Promise<string> {
  const [dbOutcome, messageOutcome]: string[] = match([userMove, computerMove])
    .with(
      ["Rock", "Scissors"],
      ["Scissors", "Paper"],
      ["Paper", "Rock"],
      () => ["WIN", "You Win!!!"]
    )
    .with(
      P.when(() => userMove === computerMove),
      () => ["DRAW", "It's a Draw!"]
    )
    .otherwise(() => ["LOSE", "You lose :< "]);
  const isDbWritten = await logRes(dbOutcome);
  if (isDbWritten === null) {
    return messageOutcome;
  } else {
    return `${messageOutcome} ... but I couldn't save this result because something went wrong`;
  }
}

export async function welcome(): Promise<string[]> {
  const res = ["Wanna play? Your move (0: Rock, 1: Paper, 2: Scissors)"];
  const lastGameParsed = result.safeParse(await lastGame());
  if (lastGameParsed.success) {
    res.push(
      "Our last game timestamp is : " + lastGameParsed.data.game_date + "\n"
    );
    res.push(
      "and the game finished with this result : " +
        lastGameParsed.data.result +
        "\n"
    );
    return res;
  } else {
    res.push("Welcome to the best RPS game ever! \n");
    return res;
  }
}

export async function allGamesParsed(): Promise<ResultArray> {
  return resultArray.parse(await allGames());
}
