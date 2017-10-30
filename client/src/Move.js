import React from 'react';
import './Move.css';

function imageSrcForMove(move = '') {
  switch (move.toLowerCase()) {
    case 'rock': return 'http://1000awesomethings.com/wp-content/uploads/2008/08/rock.jpg';
    case 'paper': return 'http://1000awesomethings.com/wp-content/uploads/2008/08/paper.jpg';
    case 'scissors': return 'http://1000awesomethings.com/wp-content/uploads/2008/08/scissors.jpg';
    default: return 'https://d30y9cdsu7xlg0.cloudfront.net/png/45447-200.png';
  }
}

function imgClassName(flip) {
  return flip ? 'move flipped' : 'move';
}

export default ({ move, flip }) => (
  <img alt={move} src={imageSrcForMove(move)} className={imgClassName(flip)} />
);
