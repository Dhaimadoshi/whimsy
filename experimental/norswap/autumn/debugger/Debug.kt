package norswap.autumn.debugger

import norswap.autumn.Grammar
import norswap.autumn.naive.Parser

class Debug (val instance: Parser, g: Grammar, p: ()-> Boolean): Parser()
{
    init { grammar = g; parser = p; }

    override fun invoke(): Boolean {
        val node = buildST()

        val result = parser()
        node.pos = grammar.pos;
        return result;
    }

    // create a syntax node and push it on the stack
    fun buildST(): STNode {
        val node = STNode(instance, grammar.pos)
        grammar.syntax_tree.push(node)
        return node
    }
}