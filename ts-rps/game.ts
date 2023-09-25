import * as readline from "node:readline/promises";
import { stdin as input, stdout as output, stdout } from "node:process";

//createInterface is a callable of Node's readLine module (see documentation)
//In this way, we can connect stdin and stdout
const rl = readline.createInterface({ input, output });

function generateComputerMove() {
  return String(Math.round(Math.random() * 2));
}

export async function play(question: string): Promise<void> {
  const computerMove = generateComputerMove();
  const userMove = await rl.question(question);

  stdout.write(`You chose:  ${userMove}\nComputer chosed:  ${computerMove}\n`);
  if (userMove === computerMove) {
    stdout.write(`Draw!\n`);
  } else if (
    (userMove === "0" && computerMove === "2") ||
    (userMove === "2" && computerMove === "1") ||
    (userMove === "1" && computerMove === "0")
  ) {
    stdout.write(`You win!\n`);
  } else {
    stdout.write(`You lose :<\n`);
  }
  rl.close();
}
