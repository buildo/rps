package rps.model

import io.buildo.enumero.annotations.enum

@enum trait Result {
  object PlayerWin
  object CPUWin
  object Draw
}