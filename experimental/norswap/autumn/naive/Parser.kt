package norswap.autumn.naive
import norswap.autumn.Grammar
import norswap.autumn.debugger.debugNode
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.javaType

abstract class Parser : () -> Boolean
{
    lateinit var grammar: Grammar
    lateinit var parser: ()-> Boolean
    var rule = ""
    val type = this.javaClass.simpleName!! // this is always a class

    override fun invoke(): Boolean {
        if(grammar.DEBUG)
            return debugNode(grammar, this)
        else
            return parser()
    }

    fun children(): ArrayList<Parser> {
        val children = arrayListOf<Parser>()
        this::class.declaredMemberProperties.forEach {
            if (it.returnType == Parser::class.starProjectedType)
                children.add(readPropery(this, it.name))

            if (it.returnType.javaType == Array<out Parser>::class.javaObjectType) {
                val parsers: Array<Parser> = readPropery(this, it.name)
                parsers.forEach {children.add(it)}
            }
        }
        return children
    }
}

fun <R: Any?> readPropery(instance: Any, propertyName: String): R {
    val clazz = instance.javaClass.kotlin
    @Suppress("UNCHECKED_CAST")
    return clazz.declaredMemberProperties.first { it.name == propertyName }.get(instance) as R
}