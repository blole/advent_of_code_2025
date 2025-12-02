package lib

import java.math.BigDecimal
import java.math.BigInteger

inline fun <reified T: Any> MatchResult.get(i: Int): T =
    when (T::class) {
        String::class -> groupValues[i] as T
        Int::class -> groupValues[i].toInt() as T
        Long::class -> groupValues[i].toLong() as T
        Float::class -> groupValues[i].toFloat() as T
        Double::class -> groupValues[i].toDouble() as T
        BigInteger::class -> groupValues[i].toBigInteger() as T
        BigDecimal::class -> groupValues[i].toBigDecimal() as T
        Char::class -> (groupValues[i].also { assert(it.length == 1) }.first() as T)
        else -> throw NotImplementedError("missing regex conversion for ${T::class}")
    }

fun String.match0(regex: String): String? =
    Regex(regex).matchEntire(this)?.value

inline fun <reified A : Any>
        String.match1(regex: String): A? =
    Regex(regex).matchEntire(this)?.run { get(1) }

inline fun <reified A : Any, reified B : Any>
        String.match2(regex: String): Pair<A,B>? =
    Regex(regex).matchEntire(this)?.run { Pair(get(1), get(2)) }

inline fun <reified A : Any, reified B : Any, reified C : Any>
        String.match3(regex: String): Triple<A,B,C>? =
    Regex(regex).matchEntire(this)?.run { Triple(get(1), get(2), get(3)) }

fun <R> String.destructure(regex: String, transform: () -> R): R? =
    Regex(regex).matchEntire(this)?.run { transform() }

inline fun <reified A : Any, R>
        String.destructure(regex: String, transform: (a: A) -> R): R? =
    Regex(regex).matchEntire(this)?.run { transform(get(1)) }

inline fun <reified A : Any, reified B : Any, R>
        String.destructure(regex: String, transform: (a: A, b: B) -> R): R? =
    Regex(regex).matchEntire(this)?.run { transform(get(1), get(2)) }

inline fun <reified A : Any, reified B : Any, reified C : Any, R>
        String.destructure(regex: String, transform: (a: A, b: B, c: C) -> R): R? =
    Regex(regex).matchEntire(this)?.run { transform(get(1), get(2), get(3)) }

inline fun <reified A : Any, reified B : Any, reified C : Any, reified D : Any, R>
        String.destructure(regex: String, transform: (a: A, b: B, c: C, d: D) -> R): R? =
    Regex(regex).matchEntire(this)?.run { transform(get(1), get(2), get(3), get(4)) }

inline fun <reified A : Any, reified B : Any, reified C : Any, reified D : Any, reified E : Any, R>
        String.destructure(regex: String, transform: (a: A, b: B, c: C, d: D, e: E) -> R): R? =
    Regex(regex).matchEntire(this)?.run { transform(get(1), get(2), get(3), get(4), get(5)) }



class DestructuringContextBuilder<R>(val input: String) {
    val cases: MutableList<() -> Result<R>?> = mutableListOf()

    object Else
    class Result<R>(val value: R)

    infix fun Else.then(transform: (String) -> R) = "(.*)" then transform
    infix fun
            String.then(transform: () -> R) {
        cases += { Regex(this).matchEntire(input)?.run { Result(transform()) } }
    }
    inline infix fun <reified A : Any>
            String.then(crossinline transform: (A) -> R) {
        cases += { Regex(this).matchEntire(input)?.run { Result(transform(get(1))) } }
    }
    inline infix fun <reified A : Any, reified B : Any>
            String.then(crossinline transform: (A,B) -> R) {
        cases += { Regex(this).matchEntire(input)?.run { Result(transform(get(1), get(2))) } }
    }
    inline infix fun <reified A : Any, reified B : Any, reified C : Any>
            String.then(crossinline transform: (A,B,C) -> R) {
        cases += { Regex(this).matchEntire(input)?.run { Result(transform(get(1), get(2), get(3))) } }
    }

    companion object {
        private fun <R> whenRegexPrivate(input: String, defineCases: DestructuringContextBuilder<R>.() -> Unit): Result<R>? {
            val builder = DestructuringContextBuilder<R>(input)
            builder.defineCases()
            for (case in builder.cases) {
                val result = case()
                if (result != null)
                    return result
            }
            return null
        }

        fun <R> whenRegexOrNull(input: String, defineCases: DestructuringContextBuilder<R>.() -> Unit): R? {
            return whenRegexPrivate(input, defineCases)?.value
        }

        fun <R> whenRegex(input: String, defineCases: DestructuringContextBuilder<R>.() -> Unit): R {
            return (whenRegexPrivate(input, defineCases) ?: throw IllegalArgumentException("no match in '$input'")).value
        }
    }
}
