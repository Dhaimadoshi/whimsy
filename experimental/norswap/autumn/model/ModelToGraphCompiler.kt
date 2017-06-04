package norswap.autumn.model

import norswap.lang.java8.Java8Model
import norswap.lang.java_base.escape
import norswap.utils.camel_to_snake
import norswap.utils.plusAssign
import norswap.utils.poly.Poly1
import norswap.utils.snake_to_camel
import norswap.utils.supers
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

var named_rule = listOf<String>()

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
    b += "import norswap.autumn.naive.AssocLeft\n"
    b += "import norswap.autumn.naive.NotAhead\n"
    b += "import norswap.lang.java_base.*\n"
    b += "import norswap.lang.java8.ast.*\n"
    b += "import norswap.lang.java8.ast.TypeDeclKind.*\n\n"

    b += "class $klass_name: TokenGrammar()\n{"

    model::class.java.methods
            .filter { supers <Builder> (it.returnType) }
            .forEach { named_rule += it.name.kotlin_getter_to_val_name() }

    builders(model).forEach {
        b += "\n\n"
        b += compile_graph_top_level(it)
    }

    b += "\n\n    override fun root() = root.invoke()"
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

fun Poly1<ParserBuilder, String>.digesto(p: ParserBuilder): String
{
    top_level = false
//    val func = if (!val_parsers.contains(p::class.java)) "()" else ""
//    return p.name ?. let { "$it$func" } ?: invoke(p)
    return p.name ?: invoke(p)
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
//        val func = !val_parsers.contains(it::class.java)
        val str = model_graph_compiler(it)
        val b = StringBuilder()
        b += "    "

//        if (overrides.contains(it.name))
//            b += "override "

//        if (func)   b += "fun ${it.name}()"
//        else        b += "val ${it.name}"
        b += "val ${it.name}"

        if (it.attributes.contains(TypeHint)) b += " : Parser"

//        if (equal_same_line.contains(it::class.java))
            b += " = $str"
//        else
//            b += "\n        = $str"

//        b += "()"

        b.toString()
    }
}

// TODO check what to do with Token grammar dependend parser
val model_graph_compiler = Poly1 <ParserBuilder, String>().apply {

    // TODO : ref to class or ref to val ?
    on <ReferenceBuilder> {
        if (named_rule.contains(it.str))
            it.str.camel_to_snake()
        else
            it.str.snake_to_camel() + "(this)"
    }

    on <ParserCodeBuilder> {
        it.code
    }

    on <StrBuilder> {
        "Str(this, \"${it.str.escape()}\")"
    }

    on <WordBuilder> {
        "WordString(this, \"${it.str.escape()}\")"
    }

    on <StrTokenBuilder> {
        "StrToken(this, \"${it.str.escape()}\")"
    }

    on <TokenBuilder> {
        "Token(this, { ${it.value} }, ${digesto(it.child)})"
    }


    on <KeywordBuilder> {
        "Kword(this, \"${it.str.escape()}\")"
    }

    on <CharRangeBuilder> {
        "CharRng(this, '${it.start}', '${it.end}')"
    }

    on <CharSetBuilder> {
        "CharSet(this, \"${it.str.escape()}\")"
    }

    on <SeqBuilder> {
        val children = it.list
                .map { digesto(it) + "()" }
                .joinToString(separator = "&& ")
        "Seq(this, {$children })"
    }

    on <ChoiceBuilder> {
        val children = it.list
                .map { digesto(it) + "()" }
                .joinToString(separator = " || ")
                .replace("\n", "\n             ")
        "Choice(this, {$children})"
    }

    on <LongestBuilder> {
        val children = it.list
                // use short form when possible (`name`, not `{ name() }`)
                //.map { it.name ?: "{ ${digesto(it)} }" }
                .map { digesto(it) }
                .joinToString(separator = ", ")
        // TODO Check if this is correct, not sure for the grammar receiver (this)
        "Longest(this, arrayListOf($children) )"
    }

    on <TokenChoiceBuilder> {
        val children = it.list
                .map { digesto(it) }
                .joinToString()
        "TokenChoice(this, $children)"
    }

    on <AheadBuilder> {
        "Ahead(this, ${digesto(it.child)})"
    }

    on <NotBuilder> {
        "NotAhead(this, ${digesto(it.child)})"
    }

    on <OptBuilder> {
        "Opt(this, ${digesto(it.child)})"
    }
    on <MaybeBuilder> {
        "Maybe(this, ${digesto(it.child)})"
    }
    on <AsBoolBuilder> {
        "AsBool(this, ${digesto(it.child)})"
    }

    on <Repeat0Builder> {
        "Repeat0(this, ${digesto(it.child)})"
    }

    on <Repeat1Builder> {
        "Repeat1(this, ${digesto(it.child)})"
    }

    on <AnglesBuilder> {
        "Angles(this, ${digesto(it.child)})"
    }

    on <CurliesBuilder> {
        "Curlies(this, ${digesto(it.child)})"
    }

    on <SquaresBuilder> {
        "Squares(this, ${digesto(it.child)})"
    }

    on <ParensBuilder> {
        "Parens(this, ${digesto(it.child)})"
    }

    on <PlainTokenBuilder> {
        "Token(this, ${digesto(it.child)})"
    }

    on <EmptyAnglesBuilder>  { "AnglesEmpty(this)" }
    on <EmptyCurliesBuilder> { "CurliesEmpty(this)" }
    on <EmptySquaresBuilder> { "SquaresEmpty(this)" }
    on <EmptyParensBuilder>  { "ParensEmpty(this)" }

    on <CommaList0Builder> {
        "CommaList0(this, ${digesto(it.child)})"
    }
    on <CommaList1Builder> {
        "CommaList1(this, ${digesto(it.child)})"
    }

    on <CommaListTerm0Builder> {
        "CommaListTerm0(this, ${digesto(it.child)})"
    }
    on <CommaListTerm1Builder> {
        "CommaListTerm1(this, ${digesto(it.child)})"
    }

    on <AsValBuilder> {
        "AsVal(this, ${it.value}, ${digesto(it.child)} )"
    }

    on <RepeatNBuilder> {
        "Repeat(this, ${it.n}, ${digesto(it.child)} )"
    }


    on <Around0Builder> {
        "Around0(this, ${digesto(it.around)}, ${digesto(it.inside)})"
    }

    on <Around1Builder> {
        "Around1(this, ${digesto(it.around)}, ${digesto(it.inside)})"
    }

    on <Until0Builder> {
        "Until0(this, ${digesto(it.repeat)}, ${digesto(it.terminator)})"
    }

    on <Until1Builder> {
        "Until1(this, ${digesto(it.repeat)}, ${digesto(it.terminator)})"
    }

    on <BuildBuilder> {
        if (top_level)
            "Build(this,\n" +
                    "        ${it.backlog},\n" +
                    "        syntax = ${digesto(it.child)},\n" +
                    "        effect = {${it.effect.replace("\n", "\n" + " ".repeat(19))}})"
        else
            "\nBuild(this, ${it.backlog}, ${digesto(it.child)}, {${it.effect}})"
    }

    on <AffectBuilder> {
        "Affect(this,\n" +
                "        ${it.backlog},\n" +
                "        syntax = ${digesto(it.child)},\n" +
                "        effect = {${it.effect}})"
    }

    on <BuildStrBuilder> {
        "BuildStr(this,\n" +
                "        syntax = ${digesto(it.child)},\n" +
                "        value = {${it.effect}})"
    }

    // TODOclass AssocLeftBuilder: ParserBuilder()
//    {
//        val operators = ArrayList<OperatorBuilder>()
//
//        var left: ParserBuilder? = null
//        var right: ParserBuilder? = null
//        var operands: ParserBuilder? = null
//        var strict: Boolean? = null

    // class AssocLeft (g: Grammar, val init: AssocLeft.() -> Unit): Parser()
    on <AssocLeftBuilder> {
        val b = StringBuilder()

        if (it is AssocLeftBuilder)
            b += "AssocLeft(this) { \n"

        if (it.operands != null && (it.left != null || it.right != null))
            throw Error("Cannot set both operands and left/right.")

        if (it.strict != null)
            b += "        strict = ${it.strict!!}\n"
        if (it.operands != null)
            b += "        operands = { ${digesto(it.operands!!)}() }\n"
        if (it.left != null)
            b += "        left = { ${digesto(it.left!!)}() }\n"
        if (it.right != null)
            b += "        right = { ${digesto(it.right!!)}() }\n"

        it.operators.forEach {
            b += "        ${it.kind}({ ${digesto(it.parser)}() }, { ${it.effect} })\n"
        }

        b += "    }"
        b.toString()
    }
}

// -------------------------------------------------------------------------------------------------

