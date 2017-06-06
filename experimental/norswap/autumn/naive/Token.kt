package norswap.autumn.naive

import norswap.autumn.Parser
import norswap.autumn.TokenGenerator
import norswap.autumn.TokenGrammar

class Token (val g: TokenGrammar, val value: TokenGenerator?, val tkn: Parser): NaiveParser() {
    init { grammar = g }
    constructor (g: TokenGrammar, str: String): this(g, null, {Str(g, str)()})
    constructor (g: TokenGrammar, token: Parser): this(g, null, token)

    val token = ( value?.let { g.token(value, tkn) }?: g.token { tkn() } ) as TokenGrammar.TokenParser
    override fun invoke() = token()
}

class Kword(val g: TokenGrammar, val kword: String): NaiveParser() {
    init { grammar = g }
    val keyword = g.token ({ null }) { Str(grammar, kword)() }
    override fun invoke() = keyword()
}

class TokenChoice (val g: TokenGrammar, vararg val tokens: TokenGrammar.TokenParser): NaiveParser() {
    init { grammar = g }
    override fun invoke() = g.token_choice(*tokens)()
}