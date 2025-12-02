import lib.DestructuringContextBuilder.Companion.whenRegex
import lib.Kattio
import kotlin.math.abs
import kotlin.sequences.count

fun Iterable<LongRange>.day02a() =
  asSequence()
    .flatMap { it.asSequence() }
    .filter {
      "$it".matches(Regex("(\\d+)\\1"))
    }
    .sum()

fun Iterable<LongRange>.day02b() =
  asSequence()
    .flatMap { it.asSequence() }
    .filter {
      "$it".matches(Regex("(\\d+)\\1+"))
    }
    .sum()

fun main() {
  val io = Kattio()
  val idRanges = io
    .words(",")
    .map { rangeString ->
      whenRegex(rangeString) {
        "(\\d+)-(\\d+)" then { from: Long, to: Long -> LongRange(from, to) }
      }
    }
    .toList()

  println(idRanges.day02a())
  println(idRanges.day02b())
}
