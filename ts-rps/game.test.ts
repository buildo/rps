import { expect, test } from "vitest";
import { playLogic } from "./game";
import { read } from "./model/Move";

test("game logic test", async () => {
  expect(await playLogic("Rock", "Paper")).toContain("You lose :<");
  expect(await playLogic("Rock", "Scissors")).toContain("You Win!!!");
  expect(await playLogic("Paper", "Rock")).toContain("You Win!!!");
  expect(await playLogic("Paper", "Scissors")).toContain("You lose :<");
  expect(await playLogic("Scissors", "Rock")).toContain("You lose :<");
  expect(await playLogic("Scissors", "Paper")).toContain("You Win!!!");
});

test("read function test", () => {
  expect(read("0")).toBe("Rock");
  expect(read("1")).toBe("Paper");
  expect(read("2")).toBe("Scissors");

  const randomNum = String(Math.round(Math.random()) + 3);
  expect(() => read(randomNum)).toThrowError();
});
