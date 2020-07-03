# rps-ts

This is a simple demonstration of a typescript backend setup
The app consist in the rps game

In order to run it, setup the database:

```
cd db && docker-compose up -d
```

Install packages:
`yarn`

Run The Server:
`yarn start`

Play a game with an HTTP client:

POST `http://localhost:8080/play`
```
{
"move": "Rock"
}
```

retrieve last game result
GET `http://localhost:8080/play`