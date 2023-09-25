import { expect, test } from "vitest";
import { playLogic } from "./game";
import { read } from "./model/Move";

test("game logic test", () => {
  expect(playLogic("Rock", "Paper")).toBe("Lose");
  expect(playLogic("Rock", "Scissors")).toBe("Win");
  expect(playLogic("Paper", "Rock")).toBe("Win");
  expect(playLogic("Paper", "Scissors")).toBe("Lose");
  expect(playLogic("Scissors", "Rock")).toBe("Lose");
  expect(playLogic("Scissors", "Paper")).toBe("Win");
});

test("read function test", () => {
  expect(read("0")).toBe("Rock");
  expect(read("1")).toBe("Paper");
  expect(read("2")).toBe("Scissors");

  const randomNum = String(Math.round(Math.random()) + 3);
  expect(() => read(randomNum)).toThrowError();
});
