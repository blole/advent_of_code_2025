package lib

import lib.Tokenizer.Companion.tokenize
import java.io.*
import java.math.BigInteger

class Tokenizer(val line: String) {
    private var start = 0

    private fun nextTokenRange(delimiters: String? = null): IntRange? {

        var i = start

        if (delimiters == null) {
            while (i < line.length && line[i].isWhitespace()) i++
        } else {
            while (i < line.length && line[i] in delimiters) i++
        }

        val from = i

        if (delimiters == null) {
            while (i < line.length && !line[i].isWhitespace()) i++
        } else {
            while (i < line.length && line[i] !in delimiters) i++
        }

        return (from until i).takeIf { !it.isEmpty() }
    }

    fun hasMoreTokens(delimiters: String? = null): Boolean = nextTokenRange(delimiters) != null

    fun peekToken(delimiters: String? = null): String? {
        if (start == 0 && delimiters == "" && line.length == 0)
            return "".also { start = 1 }

        val nextTokenRange = nextTokenRange(delimiters) ?: return null
        return line.substring(nextTokenRange)
    }

    fun nextToken(delimiters: String? = null): String? {
        if (start == 0 && delimiters == "" && line.length == 0)
            return "".also { start = 1 }

        val nextTokenRange = nextTokenRange(delimiters) ?: return null
        start = nextTokenRange.last + 1
        return line.substring(nextTokenRange)
    }

    companion object {
        fun String.tokenize(): Tokenizer = Tokenizer(this)
    }
}

class Kattio(i: InputStream = System.`in`, o: OutputStream = System.out) : PrintWriter(BufferedOutputStream(o)) {
    private val reader: BufferedReader = BufferedReader(InputStreamReader(i))

    private var tokenizer: Tokenizer? = null
        get() {
            if (field == null)
                field = reader.readLine()?.tokenize()
            return field
        }

    private fun nextToken(delimiters: String? = null): String? {
        while (tokenizer != null) {
            val token = tokenizer!!.nextToken(delimiters)
            if (token != null)
                return token
            else
                tokenizer = null
        }
        return null
    }

    val hasMoreTokens: Boolean get() = tokenizer?.hasMoreTokens() ?: false

    fun intOrNull(delimiters: String? = null):    Int?        = nextToken(delimiters)?.toInt()
    fun longOrNull(delimiters: String? = null):   Long?       = nextToken(delimiters)?.toLong()
    fun bigintOrNull(delimiters: String? = null): BigInteger? = nextToken(delimiters)?.toBigInteger()
    fun doubleOrNull(delimiters: String? = null): Double?     = nextToken(delimiters)?.toDouble()
    fun wordOrNull(delimiters: String? = null):   String?     = nextToken(delimiters)
    fun lineOrNull():   String?     = nextToken("")

    fun int(delimiters: String? = null):    Int        = intOrNull(delimiters)!!
    fun long(delimiters: String? = null):   Long       = longOrNull(delimiters)!!
    fun bigint(delimiters: String? = null): BigInteger = bigintOrNull(delimiters)!!
    fun double(delimiters: String? = null): Double     = doubleOrNull(delimiters)!!
    fun word(delimiters: String? = null):   String     = wordOrNull(delimiters)!!
    fun line():   String     = lineOrNull()!!

    fun ints(delimiters: String? = null):    Sequence<Int>        = generateSequence { intOrNull(delimiters) }
    fun longs(delimiters: String? = null):   Sequence<Long>       = generateSequence { longOrNull(delimiters) }
    fun bigints(delimiters: String? = null): Sequence<BigInteger> = generateSequence { bigintOrNull(delimiters) }
    fun doubles(delimiters: String? = null): Sequence<Double>     = generateSequence { doubleOrNull(delimiters) }
    fun words(delimiters: String? = null):   Sequence<String>     = generateSequence { wordOrNull(delimiters) }
    fun lines():   Sequence<String>     = generateSequence { lineOrNull() }
}
