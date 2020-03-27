package rps.model

import java.util.UUID
import java.time.Instant

case class PlayResponse(id: UUID, userMove: Move, computerMove: Move, result: Result, createdAt: Instant)
