import lib.Kattio
import kotlin.math.max

fun Iterable<List<Int>>.day03a(): Int =
  sumOf { joltages ->
    joltages
      .runningFold(0, ::max)
      .zip(joltages)
      .maxOf { it.first * 10 + it.second }
  }

fun Iterable<List<Int>>.day03b(): Long =
  sumOf { joltages ->
    joltages
      .fold(List(12) { 0 }) { best, joltage ->
        val new = best.toMutableList()
        new += joltage
        val dropAt = new
          .zipWithNext()
          .indexOfFirst { it.first < it.second }
        if (dropAt == -1)
          new.removeAt(new.lastIndex)
        else
          new.removeAt(dropAt)
        new
      }
      .joinToString("")
      .toLong()
  }

fun main() {
  val io = Kattio()
  val batteryBanks = io
    .lines()
    .map { line -> line.map { it.digitToInt() } }
    .toList()

  println(batteryBanks.day03a())
  println(batteryBanks.day03b())
}
