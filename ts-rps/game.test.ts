import { expect, test, vi } from "vitest";
import { playLogic, welcome } from "./game";
import { read } from "./model/Move";

vi.mock("./sql/db", () => {
  const logRes = vi.fn();
  logRes.mockResolvedValue("");
  const lastGame = vi.fn();
  lastGame.mockReturnValueOnce({
    game_date: "2023-09-14 10:08:15.151443",
    result: "blblbl",
  });
  lastGame.mockReturnValue({
    game_date: "2023-09-14 10:08:15.151443",
    result: "WIN",
  });

  return {
    logRes,
    lastGame,
  };
});

test("game logic test", async () => {
  expect(await playLogic("Rock", "Paper")).toContain("You lose :< ");
  expect(await playLogic("Rock", "Scissors")).toContain("You Win!!!");
  expect(await playLogic("Paper", "Rock")).toContain("You Win!!!");
  expect(await playLogic("Paper", "Scissors")).toContain("You lose :< ");
  expect(await playLogic("Scissors", "Rock")).toContain("You lose :< ");
  expect(await playLogic("Scissors", "Paper")).toContain("You Win!!!");
});

test("welcome test", () => {
  welcome().then((arr) => expect(arr.length).toBe(2));
  welcome().then((arr) => expect(arr.length).toBe(3));
  welcome().then((arr) => expect(arr[2]).toContain("WIN"));
});

test("read function test", () => {
  expect(read("0")).toBe("Rock");
  expect(read("1")).toBe("Paper");
  expect(read("2")).toBe("Scissors");

  const randomNum = String(Math.round(Math.random()) + 3);
  expect(() => read(randomNum)).toThrowError();
});
