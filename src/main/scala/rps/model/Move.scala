package rps.model

import io.buildo.enumero.annotations.indexedEnum

@indexedEnum trait Move {
  type Index = String
  object Rock {"0"}
  object Paper {"1"}
  object Scissors {"2"}
}
