type NeverPlayed = {
  type: "NeverPlayed";
};
type DBError = {
  type: "DBError";
  message: string;
};
export type Error = NeverPlayed | DBError;
