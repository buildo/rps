# rps
Rock Paper Scissor - an exercise for Scala learners 

## API Contract

```javascript
POST /rps/play
Content-Type: application/json

Request:
{
  "userMove": "Scissors" // or "Rock", or "Paper"
}

Response:
{
  "userMove": "Scissors",
  "computerMove": "Paper",
  "result": "Win" // or "Lose", or "Draw"
}
```
