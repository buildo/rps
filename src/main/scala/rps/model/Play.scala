package rps.model

import java.util.UUID
import java.time.Instant

case class Play(id: UUID, userMove: Move, computerMove: Move, result: Result, createdAt: Instant)
