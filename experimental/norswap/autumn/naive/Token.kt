package norswap.autumn.naive

import norswap.autumn.TokenGenerator
import norswap.autumn.TokenGrammar
import norswap.autumn.parsers.string
import kotlin.reflect.full.functions


class Token (val g: TokenGrammar, val value: TokenGenerator?, val token: Parser): Parser() {
    constructor (g: TokenGrammar, str: String): this(g, null, Str(str))
    constructor (g: TokenGrammar, token: Parser): this(g, null, token)
    override fun invoke() = value?.let { g.token(value) {token() }() }?: g.token { token() }()
}

class StrToken (val g: TokenGrammar, val token: String): Parser() {
    override fun invoke() = g.token { grammar.string(token) }()
}

class Kword(val g: TokenGrammar, val keyword: Parser): Parser() {
    override fun invoke() = g.token ({ null }) { keyword() }()
}

class TokenChoice (val g: TokenGrammar, vararg val tokens: Parser): Parser() {
    override fun invoke() = g.token_choice(*tokens)()
}