package norswap.lang.java8.benchmark
import norswap.autumn.CaughtException
import norswap.autumn.UncaughtException
import norswap.lang.java8.Grammar2
import norswap.lang.java8.Java8Grammar
import norswap.lang.java8.JavaNaiveGrammar
import norswap.utils.glob
import norswap.utils.read_file
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import kotlin.system.measureNanoTime

// -------------------------------------------------------------------------------------------------

fun main (args: Array<String>)
{
//    val g = Java8Grammar()
//    val g = Grammar2()
    val g = JavaNaiveGrammar()

    val os = System.getProperty("os.name")

    val corpus =
        if (os == "Mac OS X")
            "/Users/dhai/Dropbox/thesis/spring" // 14.9
        else
            "D:/spring" // 21.8

    val paths = glob("**/*.java", Paths.get(corpus))

    val slices = 100
    var time = 0L

    paths.each(slices) { i, it ->

        val input = read_file(it.toString())
        var result = false

        time += measureNanoTime {
            result = g.parse(input)
        }

        if (!result) {
            println("----------------------------------------")
            println("$i/${paths.size} -> $it")
            println("failure at (${g.input.string(g.fail_pos)}): " + g.failure?.invoke())
            val failure = g.failure
            if (failure is UncaughtException)
                failure.e.printStackTrace()
            if (failure is CaughtException)
                failure.e.printStackTrace()
            println("----------------------------------------")
            throw Break()
        }

        time += measureNanoTime {
            g.reset()
        }
    }

    println("Code parsed in: " + Duration.ofNanos(time))
}

// -------------------------------------------------------------------------------------------------

class Break : Exception()

// -------------------------------------------------------------------------------------------------

fun List<Path>.each (slices: Int, f: (Int, Path) -> Unit)
{
    val slice = (size + slices - 1) / slices
    var next_slice = slice
    var percentage: Int
    var i = 0

    try {
        forEach {
            ++i
            f(i, it)
            if (i >= next_slice) {
                percentage = ((i.toDouble() / size) * 100).toInt()
                println("$percentage% ($i/$size)")
                next_slice += slice
            }
        }
    }
    catch (e: Break) {}
}

// -------------------------------------------------------------------------------------------------