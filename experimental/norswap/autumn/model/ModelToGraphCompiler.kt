package norswap.autumn.model

import norswap.lang.java8.Java8Model
import norswap.utils.plusAssign
import norswap.utils.poly.Poly1
import java.io.PrintWriter


// -------------------------------------------------------------------------------------------------
// create a grammar which generate a tree of naive parser

fun main (args: Array<String>)
{
    val str = compile_model("NaiveGrammar2", Java8Model())
    val writer = PrintWriter("src/norswap/lang/java8/NaiveGrammar2.kt")
    writer.println(str)
    writer.flush()
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


val compile_graph__top_level = Poly1<Builder, String>().apply {

    default { "" }

    on <SectionBuilder> { "" }

    on <SeparatorBuilder> { "" }

    on <CodeBuilder> { "" }

    on <ParserBuilder> { "" }
}

val model_graph_compiler = Poly1 <ParserBuilder, String>().apply {

    on <ReferenceBuilder> { "" }

    on <ParserCodeBuilder> { "" }

    on <StrBuilder> {
        "String(${it.str.escape()})"
    }

    on <WordBuilder> { "" }

    on <StrTokenBuilder> { "" }

    on <TokenBuilder> { "" }

    on <KeywordBuilder> { "" }

    on <CharRangeBuilder> { "" }

    on <CharSetBuilder> { "" }

    on <SeqBuilder> { "" }

    on <ChoiceBuilder> { "" }

    on <LongestBuilder> { "" }

    on <TokenChoiceBuilder> { "" }

    on <AheadBuilder>   ("ahead")
    on <NotBuilder>     ("not")
    on <OptBuilder>     ("opt")
    on <MaybeBuilder>   ("maybe")
    on <AsBoolBuilder>  ("as_bool")
    on <Repeat0Builder> ("repeat0")
    on <Repeat1Builder> ("repeat1")
    on <AnglesBuilder>  ("angles")
    on <CurliesBuilder> ("curlies")
    on <SquaresBuilder> ("squares")
    on <ParensBuilder>  ("parens")
    on <PlainTokenBuilder> ("token")

    on <EmptyAnglesBuilder>  { "" }
    on <EmptyCurliesBuilder> { "" }
    on <EmptySquaresBuilder> { "" }
    on <EmptyParensBuilder>  { "" }

    on <CommaList0Builder>      ("comma_list0")
    on <CommaList1Builder>      ("comma_list1")
    on <CommaListTerm0Builder>  ("comma_list_term0")
    on <CommaListTerm1Builder>  ("comma_list_term1")

    // TODO only works for value that print to their code representation
    on <AsValBuilder> { "" }

    on <RepeatNBuilder> { "" }

    on <Around0Builder> { "" }

    on <Around1Builder> { "" }

    on <Until0Builder> { "" }

    on <Until1Builder> { "" }

    on <BuildBuilder> { "" }

    on <AffectBuilder> { "" }

    on <BuildStrBuilder> { "" }

    on <BuildStrBuilder> { "" }

    on <AssocLeftBuilder> { "" }
}

// -------------------------------------------------------------------------------------------------

