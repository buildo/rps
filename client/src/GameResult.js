import React from 'react';
import View from 'react-flexview';
import Move from './Move';

function resultText(result = '') {
  switch (result.toLowerCase()) {
    case 'win': return 'YOU WIN!';
    case 'lose': return 'YOU LOSE!';
    case 'draw': return 'IT\'S A DRAW!';
    default: return '';
  }
}

export default ({ userMove, computerMove, result }) => (
  <View column hAlignContent='center' className='game-result' width='100%' height={650}>
    <h1 style={{ color: 'white' }}>{resultText(result)}</h1>
    <View width='100%' style={{ justifyContent: 'space-between' }}>
      <Move move={userMove} flip={true} />
      <Move move={computerMove} />
    </View>
  </View>
)
