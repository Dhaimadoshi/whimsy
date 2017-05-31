package norswap.autumn.model

import norswap.lang.java8.Java8Model
import norswap.lang.java_base.escape
import norswap.utils.camel_to_snake
import norswap.utils.plusAssign
import norswap.utils.poly.Poly1
import norswap.utils.snake_to_camel
import java.io.File
import java.io.PrintWriter


// -------------------------------------------------------------------------------------------------
// create a grammar which generate a tree of naive parser

fun main (args: Array<String>)
{
    val str = compile_model_to_graph("NaiveGrammar2", Java8Model())
    val writer = PrintWriter("src/norswap/lang/java8/NaiveGrammar2.kt")
    writer.println(str)
    writer.flush()
}


// -------------------------------------------------------------------------------------------------

/**
 * Compile a grammar model to an Autumn grammar source string.
 */
fun compile_model_to_graph (klass_name: String, model: Any): String
{
    order_next = 0
    val b = StringBuilder()

    b += "package norswap.lang.java8\n"
    b += "import norswap.autumn.parsers.*\n"
    b += "import norswap.autumn.TokenGrammar\n"
    b += "import norswap.autumn.model.keyword\n"
    b += "import norswap.autumn.model.token\n"
    b += "import norswap.autumn.naive.*\n"
    b += "import norswap.autumn.naive.Not"
    b += "import norswap.lang.java_base.*\n"
    b += "import norswap.lang.java8.ast.*\n"
    b += "import norswap.lang.java8.ast.TypeDeclKind.*\n\n"

    b += "class $klass_name: TokenGrammar()\n{"

    builders(model).forEach {
        b += "\n\n"
        b += compile_graph_top_level(it)
    }

    b += "\n}"
    return b.toString()
}


fun String.escape(): String
{
    val b = StringBuilder()
    this.forEach {
        b += when (it) {
            '\"' -> "\\\""
            '\\' -> "\\\\"
            '\n' -> "\\n"
            '\r' -> "\\r"
            else -> it
        }
    }
    return b.toString()
}


val compile_graph_top_level = Poly1<Builder, String>().apply {

    default { "" }

    on <SectionBuilder> {
        when (it.level) {
            1 ->  "    /// ${it.name!!.capitalize()} " + "=".repeat(91 - it.name!!.length)
            2 -> "    // ${it.name!!.capitalize()} " + "-".repeat(71 - it.name!!.length)
            else -> ""
        }   }

    on <SeparatorBuilder> {
        when (it.level) {
            1 ->  "    /// " + "=".repeat(92)
            2 -> "    //// " + "-".repeat(72)
            else -> ""
        }   }

    on <CodeBuilder> {
        it.code.prependIndent("    ")
    }

    on <ParserBuilder> {
        top_level = true
        val func = !val_parsers.contains(it::class.java)
        val str = model_graph_compiler(it)
        val b = StringBuilder()
        b += "    "

        if (overrides.contains(it.name))
            b += "override "

        if (func)   b += "fun ${it.name}()"
        else        b += "val ${it.name}"
//        b += "val ${it.name}"

//        if (it.attributes.contains(TypeHint))
//            if (func)   b += " : Boolean"
//            else        b += " : Parser"

        if (equal_same_line.contains(it::class.java))
            b += " = $str"
        else
            b += "\n        = $str"

        b.toString()
    }
}

// TODO check what to do with Token grammar dependend parser
val model_graph_compiler = Poly1 <ParserBuilder, String>().apply {

    // TODO : ref to class or ref to val ?
    on <ReferenceBuilder> {
        it.str.snake_to_camel() + "()"
    }

    on <ParserCodeBuilder> {
        it.code
    }

    on <StrBuilder> {
        "Str(\"${it.str.escape()}\")"
    }

    on <WordBuilder> {
        "WordString(\"${it.str.escape()}\")"
    }

    // TODO
    on <StrTokenBuilder> {
        "\"${it.str.escape()}\".token"
    }

    // TODO
    on <TokenBuilder> {
//        "token ({ ${it.value} }) { ${digest(it.child)} }"
        "TODO()"
    }


    on <KeywordBuilder> {
        "\"${it.str.escape()}\".keyword"
    }

    on <CharRangeBuilder> {
        "CharRng('${it.start}', '${it.end}')"
    }

    on <CharSetBuilder> {
        "CharSet(\"${it.str.escape()}\")"
    }

    on <SeqBuilder> {
        val children = it.list
                .map { digest(it) }
                .joinToString(separator = ", ")
        "Seq(arrayListOf<Parser>($children))"
    }

    on <ChoiceBuilder> {
        val children = it.list
                .map { digest(it) }
                .joinToString(separator = ", ")
                .replace("\n", "\n             ")
        "Choice(arrayListOf<Parser>($children))"
    }

    on <LongestBuilder> {
        val children = it.list
                // use short form when possible (`name`, not `{ name() }`)
                //.map { it.name ?: "{ ${digest(it)} }" }
                .map { digest(it) }
                .joinToString(separator = ", ")
        // TODO Check if this is correct, not sure for the grammar receiver (this)
        "Longest(this, arrayListOf<Parser>($children) )"
    }

    // TODO
    on <TokenChoiceBuilder> {
//        val children = it.list
//                .map { it.name }
//                .joinToString()
//        "token_choice($children)"
        "TODO()"
    }

    on <AheadBuilder> {
        "Ahead(${digest(it.child)})"
    }

    on <NotBuilder> {
        "Not(${digest(it.child)})"
    }

    on <OptBuilder> {
        "Opt(${digest(it.child)})"
    }
    on <MaybeBuilder> {
        "Maybe(${digest(it.child)})"
    }
    on <AsBoolBuilder> {
        "AsBool(${digest(it.child)})"
    }

    on <Repeat0Builder> {
        "Repeat0(${digest(it.child)})"
    }

    on <Repeat1Builder> {
        "Repeat1(${digest(it.child)})"
    }

    on <AnglesBuilder> {
        "Angles(${digest(it.child)})"
    }

    on <CurliesBuilder> {
        "Curlies(${digest(it.child)})"
    }

    on <SquaresBuilder> {
        "Squares(${digest(it.child)})"
    }

    on <ParensBuilder> {
        "Parens(${digest(it.child)})"
    }

    on <PlainTokenBuilder> ("token")

    on <EmptyAnglesBuilder>  { "" }
    on <EmptyCurliesBuilder> { "" }
    on <EmptySquaresBuilder> { "" }
    on <EmptyParensBuilder>  { "" }

    on <CommaList0Builder> {
        "CommaList0(${digest(it.child)})"
    }
    on <CommaList1Builder> {
        "CommaList1(${digest(it.child)})"
    }

    on <CommaListTerm0Builder> {
        "CommaListTerm0(${digest(it.child)})"
    }
    on <CommaListTerm1Builder> {
        "CommaListTerm1(${digest(it.child)})"
    }

    on <AsValBuilder> {
        "AsVal(${it.value}, ${digest(it.child)} )"
    }

    on <RepeatNBuilder> {
        "Repeat(${it.n}, ${digest(it.child)} )"
    }


    on <Around0Builder> {
        "Around0(${digest(it.around)}, ${digest(it.inside)})"
    }

    on <Around1Builder> {
        "Around1(${digest(it.around)}, ${digest(it.inside)})"
    }

    on <Until0Builder> {
        "Until0(${digest(it.repeat)}, ${digest(it.terminator)})"
    }

    on <Until1Builder> {
        "Until1(${digest(it.repeat)}, ${digest(it.terminator)})"
    }

    // TODO : Probably this section doesnt work
    // TODO : effect is not supposed to be a Parser ...
    on <BuildBuilder> {
        if (top_level)
            "Build(${it.backlog},\n" +
                    "        syntax = ${digest(it.child)},\n" +
                    "        effect = ${it.effect.replace("\n", "\n" + " ".repeat(19))})"
        else
            "\nBuild(${it.backlog}, ${digest(it.child)}, ${it.effect})"
    }

    on <AffectBuilder> {
        "Affect(${it.backlog},\n" +
                "        syntax = ${digest(it.child)},\n" +
                "        effect = ${it.effect})"
    }

    on <BuildStrBuilder> {
        "BuildStr(\n" +
                "        syntax = ${digest(it.child)},\n" +
                "        value = ${it.effect})"
    }

    // TODO
    on <AssocLeftBuilder> {
        "TODO()"
    }
}

// -------------------------------------------------------------------------------------------------

