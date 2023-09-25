import * as readline from "node:readline/promises";
import { match, P } from "ts-pattern";
import { read, Move } from "./model/Move";

export function generateComputerMove() {
  return read(String(Math.round(Math.random() * 2)));
}

export function playLogic(userMove: Move, computerMove: Move) {
  return match([userMove, computerMove])
    .returnType<string>()
    .with(
      ["Rock", "Scissors"],
      ["Scissors", "Paper"],
      ["Paper", "Rock"],
      () => "Win"
    )

    .with(
      P.when(() => userMove === computerMove),
      () => "Draw"
    )
    .otherwise(() => "Lose");
}

export async function play(
  input: NodeJS.ReadableStream,
  output: NodeJS.WritableStream
) {
  const rl = readline.createInterface({ input, output });
  const computerMove = generateComputerMove();
  const userMove = read(
    await rl.question(
      "Wanna play? Your move (0: Rock, 1: Paper, 2: Scissors) \n"
    )
  );

  output.write(`You chose:  ${userMove}\nComputer chosed:  ${computerMove}\n`);
  output.write("The result is... " + playLogic(userMove, computerMove) + " !");

  rl.close();
}
