import React, { Component } from 'react';
import View from 'react-flexview';
import GameResult from './GameResult';
import './App.css';
import Button from 'buildo-react-components/lib/button';
import 'react-flexview/lib/flexView.css';
import 'buildo-react-components/lib/button/button.css';
import fetchMock from 'fetch-mock';

function randomMove() {
  const moves = ['Rock', 'Paper', 'Scissors'];
  return moves[Math.floor(Math.random() * moves.length)];
}

global.userMove = null;

fetchMock
  .mock(
    'http://localhost:8080/rps/play',
    (url, { body }) => {
      global.userMove = JSON.parse(body).userMove;
      return 'OK';
    },
    { method: 'POST' },
  )
  .mock('http://localhost:8080/rps/result', () => ({
    userMove: global.userMove,
    computerMove: randomMove(),
    result: 'Win',
  }));

// commenting this line will play in "mock" mode
fetchMock.restore();

class App extends Component {
  state = {};

  playNewGame(move) {
    fetch('http://localhost:8080/rps/play', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userMove: move,
      }),
    }).then(() => this.getPlayedGame());
  }

  getPlayedGame() {
    fetch('http://localhost:8080/rps/result', {
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(r => r.json())
      .then(r => this.setState(r));
  }

  render() {
    return (
      <View column hAlignContent="center" className="App" height="100%">
        <GameResult {...this.state} />
        <View>
          <Button onClick={this.playNewGame.bind(this, 'Rock')}>Rock</Button>
          <Button onClick={this.playNewGame.bind(this, 'Paper')}>Paper</Button>
          <Button onClick={this.playNewGame.bind(this, 'Scissors')}>
            Scissors
          </Button>
        </View>
      </View>
    );
  }
}

export default App;
