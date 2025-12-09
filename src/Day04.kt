import lib.Kattio
import lib.pad
import lib.windowed2d
import kotlin.Int

fun Sequence<List<Char>>.day04a(): Int =
  pad(padding = 1, defaultValue = '.')
    .windowed2d(width = 3, height = 3)
    .count { kernel: List<List<Char>> ->
      kernel[1][1] == '@' && kernel.flatten().count { it == '@' } < 5
    }

fun Sequence<List<Char>>.day04b(): Int {
  var grid: Sequence<List<Char>> = this
  var removed = 0
  do {
    var removedInStep = 0
    grid = grid
      .pad(padding = 1, defaultValue = '.')
      .windowed2d(width = 3, height = 3)
      .map { kernel: List<List<Char>> ->
        return@map if (kernel[1][1] == '@' && kernel.flatten().count { it == '@' } < 5) {
          removedInStep++
          '.'
        } else if (kernel[1][1] == '@') {
          '@'
        } else {
          '.'
        }
      }
      .chunked(grid.first().size)
      .toList()
      .asSequence()
    removed += removedInStep
  } while (removedInStep != 0)
  return removed
}

fun Sequence<Iterable<Char>>.gridToString() =
  joinToString("\n") { it.joinToString("") }

fun main() {
  val io = Kattio()
  val grid = io
    .lines()
    .map { it.toList() }
    .toList()
    .asSequence()

  println(grid.day04a())
  println(grid.day04b())
}
