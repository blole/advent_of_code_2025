package lib

fun <T> Sequence<Iterable<T>>.pad(
  top: Int,
  bottom: Int,
  left: Int,
  right: Int,
  defaultValue: T,
): Sequence<List<T>> = sequence {
  val iterator = iterator()
  val first = iterator.next().toList()
  val width = first.size
  repeat(top) {
    yield(List(left + width + right) { defaultValue })
  }
  val prefix = List(left) { defaultValue }
  val postfix = List(right) { defaultValue }
  yield(prefix + first + postfix)
  for (line in iterator) {
    yield(prefix + line + postfix)
  }
  repeat(bottom) {
    yield(List(left + width + right) { defaultValue })
  }
}

fun <T> Sequence<Iterable<T>>.pad(
  padding: Int,
  defaultValue: T
): Sequence<List<T>> = pad(
  top = padding,
  bottom = padding,
  left = padding,
  right = padding,
  defaultValue = defaultValue,
)

fun <T> Iterable<Iterable<T>>.transpose(): Sequence<List<T>> = sequence {
  val iterators = map { it.iterator() }
  while (iterators.first().hasNext()) {
    yield(iterators.map { it.next() })
  }
  if (iterators.any { it.hasNext() }) {
    throw IllegalArgumentException("unequal lengths")
  }
}

fun <T> Sequence<List<T>>.windowed2d(
  width: Int,
  height: Int,
): Sequence<List<List<T>>> =
  windowed(height)
    .flatMap { rowGroup: List<List<T>> ->
      rowGroup
        .map { it.windowed(width) }
        .transpose()
    }
