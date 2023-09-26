import * as readline from "node:readline/promises";
import { match, P } from "ts-pattern";
import { read, Move } from "./model/Move";
import { result } from "./model/Result";
import { logRes, lastGame } from "./db/db";

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
    .otherwise(() => ["DEFEAT", "You lose :< "]);
  const isDbWritten = await logRes(dbOutcome);
  if (isDbWritten === null) {
    return messageOutcome;
  } else {
    return `${messageOutcome} ... but I couldn't save this result because something went wrong`;
  }
}

export async function welcome(): Promise<string> {
  let res = "Wanna play? Your move (0: Rock, 1: Paper, 2: Scissors)\n";
  const lastGameParsed = result.safeParse(await lastGame());
  if (lastGameParsed.success) {
    res = res.concat(
      "Our last game timestamp is : " +
        lastGameParsed.data.game_date +
        "\n" +
        "and the game finished with this result : " +
        lastGameParsed.data.result +
        "\n"
    );
  } else {
    res = res.concat("Welcome to the best RPS game ever! \n");
  }
  return res;
}

export async function play(
  input: NodeJS.ReadableStream,
  output: NodeJS.WritableStream
) {
  const rl = readline.createInterface({ input, output });
  const computerMove = generateComputerMove();
  const welcomeMessage = await welcome();
  const userMove = read(await rl.question(welcomeMessage));
  const result = await playLogic(userMove, computerMove);
  output.write(`You chose:  ${userMove}\nComputer chosed:  ${computerMove}\n`);
  output.write("The result is... " + result + " !");
  rl.close();
}
