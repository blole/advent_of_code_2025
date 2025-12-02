import lib.DestructuringContextBuilder.Companion.whenRegex
import lib.Kattio
import kotlin.math.abs

@JvmInline
value class Turn(val amount: Int)

fun Iterable<Turn>.day01a() =
  runningFold(50) { position: Int, turn: Turn ->
    (position + turn.amount).mod(100)
  }
    .count { it == 0 }

fun Iterable<Turn>.day01b() =
  runningFold(50) { position: Int, turn: Turn ->
    (position + turn.amount).mod(100)
  }
    .zip(this)
    .sumOf { (start: Int, turn: Turn) ->
      val end = start + turn.amount
      if (start != 0 && end <= 0)
        abs(end) / 100 + 1
      else
        abs(end) / 100
    }

fun main() {
  val io = Kattio()
  val turns = io
    .lines()
    .map { line ->
      whenRegex(line) {
        "R(\\d+)" then { amount: Int -> Turn(amount) }
        "L(\\d+)" then { amount: Int -> Turn(-amount) }
      }
    }
    .toList()
  println(turns.day01a())
  println(turns.day01b())
}
