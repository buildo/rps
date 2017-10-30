import React, { Component } from 'react';
import View from 'react-flexview';
import GameResult from './GameResult';
import './App.css';
import Button from 'buildo-react-components/lib/button';
import 'react-flexview/lib/flexView.css';
import 'buildo-react-components/lib/button/button.css';
import fetchMock from 'fetch-mock';

function randomMove() {
  const moves = ['Rock', 'Paper', 'Scissors']
  return moves[Math.floor(Math.random()*moves.length)];
}

fetchMock
  .mock(
    'http://localhost:8080/rps/play',
    (url, { body }) => ({
      userMove: JSON.parse(body).userMove,
      computerMove: randomMove(),
      result: 'Win'
    }),
    { method: 'POST' }
  );

// commenting this line will play in "mock" mode
fetchMock.restore();

class App extends Component {

  state = {}

  newGame(move) {
    fetch('http://localhost:8080/rps/play', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        userMove: move
      })
    })
    .then(r => r.json())
    .then(r => this.setState(r));
  }

  render() {
    return (
      <View column hAlignContent='center' className='App' height='100%'>
        <GameResult {...this.state} />
        <View>
          <Button onClick={this.newGame.bind(this, 'Rock')}>Rock</Button>
          <Button onClick={this.newGame.bind(this, 'Paper')}>Paper</Button>
          <Button onClick={this.newGame.bind(this, 'Scissors')}>Scissors</Button>
        </View>
      </View>
    );
  }
}

export default App;
