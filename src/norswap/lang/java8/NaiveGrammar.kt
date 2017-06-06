package norswap.lang.java8
import norswap.autumn.TokenGrammar
import norswap.autumn.naive.*
import norswap.autumn.naive.AssocLeft
import norswap.autumn.naive.NotAhead
import norswap.lang.java_base.*
import norswap.lang.java8.ast.*
import norswap.lang.java8.ast.TypeDeclKind.*

class JavaNaiveGrammar: TokenGrammar()
{

    /// LEXICAL ====================================================================================

    // Whitespace -------------------------------------------------------------

    val line_comment = Seq(this, {Str(this, "//")() && Until0(this, CharAny(this), Str(this, "\n"))() })

    val multi_comment = Seq(this, {Str(this, "/*")() && Until0(this, CharAny(this), Str(this, "*/"))() })

    val whitespace = Repeat0(this, Choice(this, {SpaceChar(this)() || line_comment() || multi_comment()}))

    // Keywords and Operators -------------------------------------------------

    val boolean = Token(this, "boolean")

    val byte = Token(this, "byte")

    val char = Token(this, "char")

    val double = Token(this, "double")

    val float = Token(this, "float")

    val int = Token(this, "int")

    val long = Token(this, "long")

    val short = Token(this, "short")

    val void = Token(this, "void")

    val abstract = Token(this, "abstract")

    val default = Token(this, "default")

    val final = Token(this, "final")

    val native = Token(this, "native")

    val private = Token(this, "private")

    val protected = Token(this, "protected")

    val public = Token(this, "public")

    val static = Token(this, "static")

    val strictfp = Token(this, "strictfp")

    val synchronized = Token(this, "synchronized")

    val transient = Token(this, "transient")

    val volatile = Token(this, "volatile")

    val `false` = Token(this, { false }, Str(this, "false"))

    val `true` = Token(this, { true }, Str(this, "true"))

    val `null` = Token(this, { Null }, Str(this, "null"))

    val assert = Kword(this, "assert")

    val `break` = Kword(this, "break")

    val case = Kword(this, "case")

    val catch = Kword(this, "catch")

    val `class` = Kword(this, "class")

    val const = Kword(this, "const")

    val `continue` = Kword(this, "continue")

    val `do` = Kword(this, "do")

    val `else` = Kword(this, "else")

    val enum = Kword(this, "enum")

    val extends = Kword(this, "extends")

    val finally = Kword(this, "finally")

    val `for` = Kword(this, "for")

    val goto = Kword(this, "goto")

    val `if` = Kword(this, "if")

    val implements = Kword(this, "implements")

    val import = Kword(this, "import")

    val `interface` = Kword(this, "interface")

    val instanceof = Kword(this, "instanceof")

    val new = Kword(this, "new")

    val `package` = Kword(this, "package")

    val `return` = Kword(this, "return")

    val `super` = Kword(this, "super")

    val switch = Kword(this, "switch")

    val `this` = Kword(this, "this")

    val throws = Kword(this, "throws")

    val `throw` = Kword(this, "throw")

    val `try` = Kword(this, "try")

    val `while` = Kword(this, "while")

    val `!` = Kword(this, "!")

    val `%` = Kword(this, "%")

    val `%=` = Kword(this, "%=")

    val `&` = Kword(this, "&")

    val `&&` = Kword(this, "&&")

    val `&=` = Kword(this, "&=")

    val `(` = Kword(this, "(")

    val `)` = Kword(this, ")")

    val `*` = Kword(this, "*")

    val `*=` = Kword(this, "*=")

    val `+` = Kword(this, "+")

    val `++` = Kword(this, "++")

    val `+=` = Kword(this, "+=")

    val `,` = Kword(this, ",")

    val `-` = Kword(this, "-")

    val `--` = Kword(this, "--")

    val `-=` = Kword(this, "-=")

    val `=` = Kword(this, "=")

    val `==` = Kword(this, "==")

    val `?` = Kword(this, "?")

    val `^` = Kword(this, "^")

    val `^=` = Kword(this, "^=")

    val `{` = Kword(this, "{")

    val `|` = Kword(this, "|")

    val `|=` = Kword(this, "|=")

    val `!=` = Kword(this, "!=")

    val `||` = Kword(this, "||")

    val `}` = Kword(this, "}")

    val `~` = Kword(this, "~")

    val `@` = Kword(this, "@")

    val div = Kword(this, "/")

    val dive = Kword(this, "/=")

    val gt = Kword(this, ">")

    val lt = Kword(this, "<")

    val ge = Kword(this, ">=")

    val le = Kword(this, "<=")

    val sl = Kword(this, "<<")

    val sle = Kword(this, "<<=")

    val sr = WordString(this, ">>")

    val sre = Kword(this, ">>=")

    val bsr = WordString(this, ">>>")

    val bsre = Kword(this, ">>>=")

    val lsbra = Kword(this, "[")

    val rsbra = Kword(this, "]")

    val arrow = Kword(this, "->")

    val colon = Kword(this, ":")

    val semi = Kword(this, ";")

    val dot = Kword(this, ".")

    val ellipsis = Kword(this, "...")

    val dcolon = Kword(this, "::")

    // Identifiers ------------------------------------------------------------

    val iden = Token(this, {JavaIden(this)()})

    val `_` = Str(this, "_")

    val dlit = Str(this, ".")

    val hex_prefix = Choice(this, {Str(this, "0x")() || Str(this, "0x")()})

    val underscores = Repeat0(this, `_`)

    val digits1 = Around1(this, Digit(this), underscores)

    val digits0 = Around0(this, Digit(this), underscores)

    val hex_digits = Around1(this, HexDigit(this), underscores)

    val hex_num = Seq(this, {hex_prefix() && hex_digits() })

    // Numerals - Floating Point ----------------------------------------------

    val hex_significand = Choice(this, {Seq(this, {hex_prefix() && Opt(this, hex_digits)() && dlit() && hex_digits() })() || Seq(this, {hex_num() && Opt(this, dlit)() })()})

    val exp_sign_opt = Opt(this, CharSet(this, "+-"))

    val exponent = Seq(this, {CharSet(this, "eE")() && exp_sign_opt() && digits1() })

    val binary_exponent = Seq(this, {CharSet(this, "pP")() && exp_sign_opt() && digits1() })

    val float_suffix = CharSet(this, "fFdD")

    val float_suffix_opt = Opt(this, float_suffix)

    val hex_float_lit = Seq(this, {hex_significand() && binary_exponent() && float_suffix_opt() })

    val decimal_float_lit = Choice(this, {Seq(this, {digits1() && dlit() && digits0() && Opt(this, exponent)() && float_suffix_opt() })() || Seq(this, {dlit() && digits1() && Opt(this, exponent)() && float_suffix_opt() })() || Seq(this, {digits1() && exponent() && float_suffix_opt() })() || Seq(this, {digits1() && Opt(this, exponent)() && float_suffix() })()})

    val float_literal = Token(this, { parse_float(it) }, Choice(this, {hex_float_lit() || decimal_float_lit()}))

    // Numerals - Integral ----------------------------------------------------

    val bit = CharSet(this, "01")

    val binary_prefix = Choice(this, {Str(this, "0b")() || Str(this, "0B")()})

    val binary_num = Seq(this, {binary_prefix() && Around1(this, Repeat1(this, bit), underscores)() })

    val octal_num = Seq(this, {Str(this, "0")() && Repeat1(this, Seq(this, {underscores() && OctalDigit(this)() }))() })

    val decimal_num = Choice(this, {Str(this, "0")() || digits1()})

    val integer_num = Choice(this, {hex_num() || binary_num() || octal_num() || decimal_num()})

    val integer_literal = Token(this, { parse_int(it) }, Seq(this, {integer_num() && Opt(this, CharSet(this, "lL"))() }))

    // Characters and Strings -------------------------------------------------

    val octal_escape = Choice(this, {Seq(this, {CharRng(this, '0', '3')() && OctalDigit(this)() && OctalDigit(this)() })() || Seq(this, {OctalDigit(this)() && Opt(this, OctalDigit(this))() })()})

    val unicode_escape = Seq(this, {Repeat1(this, Str(this, "u"))() && Repeat(this, 4, HexDigit(this) )() })

    val escape = Seq(this, {Str(this, "\\")() && Choice(this, {CharSet(this, "btnfr\"'\\")() || octal_escape() || unicode_escape()})() })

    val naked_char = Choice(this, {escape() || Seq(this, {NotAhead(this, CharSet(this, "'\\\n\r"))() && CharAny(this)() })()})

    val char_literal = Token(this, { parse_char(it) }, Seq(this, {Str(this, "'")() && naked_char() && Str(this, "'")() }))

    val naked_string_char = Choice(this, {escape() || Seq(this, {NotAhead(this, CharSet(this, "\"\\\n\r"))() && CharAny(this)() })()})

    val string_literal = Token(this, { parse_string(it) }, Seq(this, {Str(this, "\"")() && Repeat0(this, naked_string_char)() && Str(this, "\"")() }))

    // Literal ----------------------------------------------------------------

    val literal_syntax = TokenChoice(this, integer_literal.token, string_literal.token, `null`.token, float_literal.token, `true`.token, `false`.token, char_literal.token)

    val literal = Build(this,
            0,
            syntax = literal_syntax,
            effect = {Literal(it(0))})

    /// ANNOTATIONS ================================================================================

    val annotation_element : NaiveParser = Choice(this, {{ternary()}() || {annotation_element_list()}() || {annotation()}()})

    val annotation_inner_list = CommaListTerm0(this, {annotation_element()})

    val annotation_element_list = Build(this,
            0,
            syntax = Curlies(this, annotation_inner_list),
            effect = {AnnotationElementList(it.list())})

    val annotation_element_pair = Build(this,
            0,
            syntax = Seq(this, {iden() && `=`() && annotation_element() }),
            effect = {Pair<String, AnnotationElement>(it(0), it(1))})

    val normal_annotation_suffix = Build(this,
            1,
            syntax = Parens(this, CommaList1(this, annotation_element_pair)),
            effect = {NormalAnnotation(it(0), it.list<Pair<String, AnnotationElement>>(1))})

    val single_element_annotation_suffix = Build(this,
            1,
            syntax = Parens(this, annotation_element),
            effect = {SingleElementAnnotation(it(0), it(1))})

    val marker_annotation_suffix = Build(this,
            1,
            syntax = Opt(this, ParensEmpty(this)),
            effect = {MarkerAnnotation(it(0))})

    val annotation_suffix = Choice(this, {normal_annotation_suffix() || single_element_annotation_suffix() || marker_annotation_suffix()})

    val qualified_iden = Build(this,
            0,
            syntax = Around1(this, iden, dot),
            effect = {it.list<String>()})

    val annotation = Seq(this, {`@`() && qualified_iden() && annotation_suffix() })

    val annotations = Build(this,
            0,
            syntax = Repeat0(this, annotation),
            effect = {it.list<Annotation>()})

    /// TYPES ======================================================================================

    val basic_type = TokenChoice(this, byte.token, short.token, int.token, long.token, char.token, float.token, double.token, boolean.token, void.token)

    val primitive_type = Build(this,
            0,
            syntax = Seq(this, {annotations() && basic_type() }),
            effect = {PrimitiveType(it(0), it(1))})

    val extends_bound = Build(this,
            0,
            syntax = Seq(this, {extends() && {type()}() }),
            effect = {ExtendsBound(it(0))})

    val super_bound = Build(this,
            0,
            syntax = Seq(this, {`super`() && {type()}() }),
            effect = {SuperBound(it(0))})

    val type_bound = Maybe(this, Choice(this, {extends_bound() || super_bound()}))

    val wildcard = Build(this,
            0,
            syntax = Seq(this, {annotations() && `?`() && type_bound() }),
            effect = {Wildcard(it(0), it(1))})

    val type_args = Build(this,
            0,
            syntax = Opt(this, Angles(this, CommaList0(this, Choice(this, {{type()}() || wildcard()})))),
            effect = {it.list<Type>()})

    val class_type_part = Build(this,
            0,
            syntax = Seq(this, {annotations() && iden() && type_args() }),
            effect = {ClassTypePart(it(0), it(1), it(2))})

    val class_type = Build(this,
            0,
            syntax = Around1(this, class_type_part, dot),
            effect = {ClassType(it.list<ClassTypePart>())})

    val stem_type = Choice(this, {primitive_type() || class_type()})

    val dim = Build(this,
            0,
            syntax = Seq(this, {annotations() && SquaresEmpty(this)() }),
            effect = {Dimension(it(0))})

    val dims = Build(this,
            0,
            syntax = Repeat0(this, dim),
            effect = {it.list<Dimension>()})

    val dims1 = Build(this,
            0,
            syntax = Repeat1(this, dim),
            effect = {it.list<Dimension>()})

    val type_dim_suffix = Build(this,
            1,
            syntax = dims1,
            effect = {ArrayType(it(0), it(1))})

    val type : NaiveParser = Seq(this, {stem_type() && Opt(this, type_dim_suffix)() })

    val type_union_syntax = Around1(this, {type()}, `&`)

    val type_union = Build(this,
            0,
            syntax = type_union_syntax,
            effect = {it.list<Type>()})

    val type_bounds = Build(this,
            0,
            syntax = Opt(this, Seq(this, {extends() && type_union_syntax() })),
            effect = {it.list<Type>()})

    val type_param = Build(this,
            0,
            syntax = Seq(this, {annotations() && iden() && type_bounds() }),
            effect = {TypeParam(it(0), it(1), it(2))})

    val type_params = Build(this,
            0,
            syntax = Opt(this, Angles(this, CommaList0(this, type_param))),
            effect = {it.list<TypeParam>()})

    /// MODIFIERS ==================================================================================

    val keyword_modifier = Build(this,
            0,
            syntax = Choice(this, {public() || protected() || private() || abstract() || static() || final() || synchronized() || native() || strictfp() || default() || transient() || volatile()}),
            effect = {Keyword.valueOf(it(0))})

    val modifier = Choice(this, {annotation() || keyword_modifier()})

    val modifiers = Build(this,
            0,
            syntax = Repeat0(this, modifier),
            effect = {it.list<Modifier>()})

    /// PARAMETERS =================================================================================

    val args = Build(this,
            0,
            syntax = Parens(this, CommaList0(this, {expr()})),
            effect = {it.list<Expr>()})

    val this_parameter_qualifier = Build(this,
            0,
            syntax = Repeat0(this, Seq(this, {iden() && dot() })),
            effect = {it.list<String>()})

    val this_param_suffix = Build(this,
            2,
            syntax = Seq(this, {this_parameter_qualifier() && `this`() }),
            effect = {ThisParameter(it(0), it(1), it(2))})

    val iden_param_suffix = Build(this,
            2,
            syntax = Seq(this, {iden() && dims() }),
            effect = {IdenParameter(it(0), it(1), it(2), it(3))})

    val variadic_param_suffix = Build(this,
            2,
            syntax = Seq(this, {annotations() && ellipsis() && iden() }),
            effect = {VariadicParameter(it(0), it(1), it(2), it(3))})

    val formal_param_suffix = Choice(this, {iden_param_suffix() || this_param_suffix() || variadic_param_suffix()})

    val formal_param = Seq(this, {modifiers() && type() && formal_param_suffix() })

    val formal_params = Build(this,
            0,
            syntax = Parens(this, CommaList0(this, formal_param)),
            effect = {FormalParameters(it.list())})

    val untyped_params = Build(this,
            0,
            syntax = Parens(this, CommaList1(this, iden)),
            effect = {UntypedParameters(it.list())})

    val single_param = Build(this,
            0,
            syntax = iden,
            effect = {UntypedParameters(it.list<String>())})

    val lambda_params = Choice(this, {formal_params() || untyped_params() || single_param()})

    /// NON-TYPE DECLARATIONS ======================================================================

    val var_init : NaiveParser = Choice(this, {{expr()}() || {array_init()}()})

    val array_init = Build(this,
            0,
            syntax = Curlies(this, CommaListTerm0(this, var_init)),
            effect = {ArrayInit(it.list())})

    val var_declarator_id = Build(this,
            0,
            syntax = Seq(this, {iden() && dims() }),
            effect = {VarDeclaratorID(it(0), it(1))})

    val var_declarator = Build(this,
            0,
            syntax = Seq(this, {var_declarator_id() && Maybe(this, Seq(this, {`=`() && var_init() }))() }),
            effect = {VarDeclarator(it(0), it(1))})

    val var_decl_no_semi = Build(this,
            1,
            syntax = Seq(this, {type() && CommaList1(this, var_declarator)() }),
            effect = {VarDecl(it(0), it(1), it.list(2))})

    val var_decl_suffix = Seq(this, {var_decl_no_semi() && semi() })

    val var_decl = Seq(this, {modifiers() && var_decl_suffix() })

    val throws_clause = Build(this,
            0,
            syntax = Opt(this, Seq(this, {throws() && CommaList1(this, type)() })),
            effect = {it.list<Type>()})

    val block_or_semi = Choice(this, {{block()}() || AsVal(this, null, semi )()})

    val method_decl_suffix = Build(this,
            1,
            syntax = Seq(this, {type_params() && type() && iden() && formal_params() && dims() && throws_clause() && block_or_semi() }),
            effect = {MethodDecl(it(0), it(1), it(2), it(3), it(4), it(5), it(6), it(7))})

    val constructor_decl_suffix = Build(this,
            1,
            syntax = Seq(this, {type_params() && iden() && formal_params() && throws_clause() && {block()}() }),
            effect = {ConstructorDecl(it(0), it(1), it(2), it(3), it(4), it(5))})

    val init_block = Build(this,
            0,
            syntax = Seq(this, {AsBool(this, static)() && {block()}() }),
            effect = {InitBlock(it(0), it(1))})

    /// TYPE DECLARATIONS ==========================================================================

    // Common -----------------------------------------------------------------

    val extends_clause = Build(this,
            0,
            syntax = Opt(this, Seq(this, {extends() && CommaList0(this, type)() })),
            effect = {it.list<Type>()})

    val implements_clause = Build(this,
            0,
            syntax = Opt(this, Seq(this, {implements() && CommaList0(this, type)() })),
            effect = {it.list<Type>()})

    val type_sig = Seq(this, {iden() && type_params() && extends_clause() && implements_clause() })

    val class_modified_decl = Seq(this, {modifiers() && Choice(this, {var_decl_suffix() || method_decl_suffix() || constructor_decl_suffix() || {type_decl_suffix()}()})() })

    val class_body_decl : NaiveParser = Choice(this, {class_modified_decl() || init_block() || semi()})

    val class_body_decls = Build(this,
            0,
            syntax = Repeat0(this, class_body_decl),
            effect = {it.list<Decl>()})

    val type_body = Curlies(this, class_body_decls)

    // Enum -------------------------------------------------------------------

    val enum_constant = Build(this,
            0,
            syntax = Seq(this, {annotations() && iden() && Maybe(this, args)() && Maybe(this, type_body)() }),
            effect = {EnumConstant(it(0), it(1), it(2), it(3))})

    val enum_class_decls = Build(this,
            0,
            syntax = Opt(this, Seq(this, {semi() && Repeat0(this, class_body_decl)() })),
            effect = {it.list<Decl>()})

    val enum_constants = Build(this,
            0,
            syntax = Opt(this, CommaList1(this, enum_constant)),
            effect = {it.list<EnumConstant>()})

    val enum_body = Affect(this,
            0,
            syntax = Curlies(this, Seq(this, {enum_constants() && enum_class_decls() })),
            effect = {stack.push(it(1)) ; stack.push(it(0)) /* swap */})

    val enum_decl = Build(this,
            1,
            syntax = Seq(this, {enum() && type_sig() && enum_body() }),
            effect = {val td = TypeDecl(input, ENUM, it(0), it(1), it(2), it(3), it(4), it(5))
                EnumDecl(td, it(6))})

    // Annotations ------------------------------------------------------------

    val annot_default_clause = Build(this,
            0,
            syntax = Seq(this, {default() && annotation_element() }),
            effect = {it(1)})

    val annot_elem_decl = Build(this,
            0,
            syntax = Seq(this, {modifiers() && type() && iden() && ParensEmpty(this)() && dims() && Maybe(this, annot_default_clause)() && semi() }),
            effect = {AnnotationElemDecl(it(0), it(1), it(2), it(3), it(4))})

    val annot_body_decls = Build(this,
            0,
            syntax = Repeat0(this, Choice(this, {annot_elem_decl() || class_body_decl()})),
            effect = {it.list<Decl>()})

    val annotation_decl = Build(this,
            1,
            syntax = Seq(this, {`@`() && `interface`() && type_sig() && Curlies(this, annot_body_decls)() }),
            effect = {TypeDecl(input, ANNOTATION, it(0), it(1), it(2), it(3), it(4), it(5))})

    //// ------------------------------------------------------------------------

    val class_decl = Build(this,
            1,
            syntax = Seq(this, {`class`() && type_sig() && type_body() }),
            effect = {TypeDecl(input, CLASS, it(0), it(1), it(2), it(3), it(4), it(5))})

    val interface_declaration = Build(this,
            1,
            syntax = Seq(this, {`interface`() && type_sig() && type_body() }),
            effect = {TypeDecl(input, INTERFACE, it(0), it(1), it(2), it(3), it(4), it(5))})

    val type_decl_suffix = Choice(this, {class_decl() || interface_declaration() || enum_decl() || annotation_decl()})

    val type_decl = Seq(this, {modifiers() && type_decl_suffix() })

    val type_decls = Build(this,
            0,
            syntax = Repeat0(this, Choice(this, {type_decl() || semi()})),
            effect = {it.list<Decl>()})

    /// EXPRESSIONS ================================================================================

    // Array Constructor ------------------------------------------------------

    val dim_expr = Build(this,
            0,
            syntax = Seq(this, {annotations() && Squares(this, {expr()})() }),
            effect = {DimExpr(it(0), it(1))})

    val dim_exprs = Build(this,
            0,
            syntax = Repeat1(this, dim_expr),
            effect = {it.list<DimExpr>()})

    val dim_expr_array_creator = Build(this,
            0,
            syntax = Seq(this, {stem_type() && dim_exprs() && dims() }),
            effect = {ArrayCtorCall(it(0), it(1), it(2), null)})

    val init_array_creator = Build(this,
            0,
            syntax = Seq(this, {stem_type() && dims1() && array_init() }),
            effect = {ArrayCtorCall(it(0), emptyList(), it(1), it(2))})

    val array_ctor_call = Seq(this, {new() && Choice(this, {dim_expr_array_creator() || init_array_creator()})() })

    // Lambda Expression ------------------------------------------------------

    val lambda = Build(this,
            0,
            syntax = Seq(this, {lambda_params() && arrow() && Choice(this, {{block()}() || {expr()}()})() }),
            effect = {Lambda(it(0), it(1))})

    // Expression - Primary ---------------------------------------------------

    val par_expr = Build(this,
            0,
            syntax = Parens(this, {expr()}),
            effect = {ParenExpr(it(0))})

    val ctor_call = Build(this,
            0,
            syntax = Seq(this, {new() && type_args() && stem_type() && args() && Maybe(this, type_body)() }),
            effect = {CtorCall(it(0), it(1), it(2), it(3))})

    val new_ref_suffix = Build(this,
            2,
            syntax = new,
            effect = {NewReference(it(0), it(1))})

    val method_ref_suffix = Build(this,
            2,
            syntax = iden,
            effect = {MaybeBoundMethodReference(it(0), it(1), it(2))})

    val ref_suffix = Seq(this, {dcolon() && type_args() && Choice(this, {new_ref_suffix() || method_ref_suffix()})() })

    val class_expr_suffix = Build(this,
            1,
            syntax = Seq(this, {dot() && `class`() }),
            effect = {ClassExpr(it(0))})

    val type_suffix_expr = Seq(this, {type() && Choice(this, {ref_suffix() || class_expr_suffix()})() })

    val iden_or_method_expr = Build(this,
            0,
            syntax = Seq(this, {iden() && Maybe(this, args)() }),
            effect = {it[1] ?. let { MethodCall(null, listOf(), it(0), it(1)) } ?: Identifier(it(0))})

    val this_expr = Build(this,
            0,
            syntax = Seq(this, {`this`() && Maybe(this, args)() }),
            effect = {it[0] ?. let { ThisCall(it(0)) } ?: This})

    val super_expr = Build(this,
            0,
            syntax = Seq(this, {`super`() && Maybe(this, args)() }),
            effect = {it[0] ?. let { SuperCall(it(0)) } ?: Super})

    val class_expr = Build(this,
            0,
            syntax = Seq(this, {type() && dot() && `class`() }),
            effect = {ClassExpr(it(0))})

    val primary_expr = Choice(this, {par_expr() || array_ctor_call() || ctor_call() || type_suffix_expr() || iden_or_method_expr() || this_expr() || super_expr() || literal()})

    // Expression - Postfix ---------------------------------------------------

    val dot_this = Build(this,
            1,
            syntax = `this`,
            effect = {DotThis(it(0))})

    val dot_super = Build(this,
            1,
            syntax = `super`,
            effect = {DotSuper(it(0))})

    val dot_iden = Build(this,
            1,
            syntax = iden,
            effect = {DotIden(it(0), it(1))})

    val dot_new = Build(this,
            1,
            syntax = ctor_call,
            effect = {DotNew(it(0), it(1))})

    val dot_method = Build(this,
            1,
            syntax = Seq(this, {type_args() && iden() && args() }),
            effect = {MethodCall(it(0), it(1), it(2), it(3))})

    val dot_postfix = Choice(this, {dot_method() || dot_iden() || dot_this() || dot_super() || dot_new()})

    val ref_postfix = Build(this,
            1,
            syntax = Seq(this, {dcolon() && type_args() && iden() }),
            effect = {BoundMethodReference(it(0), it(1), it(2))})

    val array_postfix = Build(this,
            1,
            syntax = Squares(this, {expr()}),
            effect = {ArrayAccess(it(0), it(1))})

    val inc_suffix = Build(this,
            1,
            syntax = `++`,
            effect = {PostIncrement(it(0))})

    val dec_suffix = Build(this,
            1,
            syntax = `--`,
            effect = {PostDecrement(it(0))})

    val postfix = Choice(this, {Seq(this, {dot() && dot_postfix() })() || array_postfix() || inc_suffix() || dec_suffix() || ref_postfix()})

    val postfix_expr = Seq(this, {primary_expr() && Repeat0(this, postfix)() })

    val inc_prefix = Build(this,
            0,
            syntax = Seq(this, {`++`() && {prefix_expr()}() }),
            effect = {PreIncrement(it(0))})

    val dec_prefix = Build(this,
            0,
            syntax = Seq(this, {`--`() && {prefix_expr()}() }),
            effect = {PreDecrement(it(0))})

    val unary_plus = Build(this,
            0,
            syntax = Seq(this, {`+`() && {prefix_expr()}() }),
            effect = {UnaryPlus(it(0))})

    val unary_minus = Build(this,
            0,
            syntax = Seq(this, {`-`() && {prefix_expr()}() }),
            effect = {UnaryMinus(it(0))})

    val complement = Build(this,
            0,
            syntax = Seq(this, {`~`() && {prefix_expr()}() }),
            effect = {Complement(it(0))})

    val not = Build(this,
            0,
            syntax = Seq(this, {`!`() && {prefix_expr()}() }),
            effect = {Not(it(0))})

    val cast = Build(this,
            0,
            syntax = Seq(this, {Parens(this, type_union)() && Choice(this, {lambda() || {prefix_expr()}()})() }),
            effect = {Cast(it(0), it(1))})

    val prefix_expr : NaiveParser = Choice(this, {inc_prefix() || dec_prefix() || unary_plus() || unary_minus() || complement() || not() || cast() || postfix_expr()})

    // Expression - Binary ----------------------------------------------------

    val mult_expr = AssocLeft(this) {
        operands = { prefix_expr() }
        op({ `*`() }, { Product(it(0), it(1)) })
        op({ div() }, { Division(it(0), it(1)) })
        op({ `%`() }, { Remainder(it(0), it(1)) })
    }

    val add_expr = AssocLeft(this) {
        operands = { mult_expr() }
        op({ `+`() }, { Sum(it(0), it(1)) })
        op({ `-`() }, { Diff(it(0), it(1)) })
    }

    val shift_expr = AssocLeft(this) {
        operands = { add_expr() }
        op({ sl() }, { ShiftLeft(it(0), it(1)) })
        op({ sr() }, { ShiftRight(it(0), it(1)) })
        op({ bsr() }, { BinaryShiftRight(it(0), it(1)) })
    }

    val order_expr = AssocLeft(this) {
        operands = { shift_expr() }
        op({ lt() }, { Lower(it(0), it(1)) })
        op({ le() }, { LowerEqual(it(0), it(1)) })
        op({ gt() }, { Greater(it(0), it(1)) })
        op({ ge() }, { GreaterEqual(it(0), it(1)) })
        postfix({ Seq(this@JavaNaiveGrammar, {instanceof() && type() })() }, { Instanceof(it(0), it(1)) })
    }

    val eq_expr = AssocLeft(this) {
        operands = { order_expr() }
        op({ `==`() }, { Equal(it(0), it(1)) })
        op({ `!=`() }, { NotEqual(it(0), it(1)) })
    }

    val binary_and_expr = AssocLeft(this) {
        operands = { eq_expr() }
        op({ `&`() }, { BinaryAnd(it(0), it(1)) })
    }

    val xor_expr = AssocLeft(this) {
        operands = { binary_and_expr() }
        op({ `^`() }, { Xor(it(0), it(1)) })
    }

    val binary_or_expr = AssocLeft(this) {
        operands = { xor_expr() }
        op({ `|`() }, { BinaryOr(it(0), it(1)) })
    }

    val and_expr = AssocLeft(this) {
        operands = { binary_or_expr() }
        op({ `&&`() }, { And(it(0), it(1)) })
    }

    val or_expr = AssocLeft(this) {
        operands = { and_expr() }
        op({ `||`() }, { Or(it(0), it(1)) })
    }

    val ternary_suffix = Build(this,
            1,
            syntax = Seq(this, {`?`() && {expr()}() && colon() && {expr()}() }),
            effect = {Ternary(it(0), it(1), it(2))})

    val ternary = Seq(this, {or_expr() && Opt(this, ternary_suffix)() })

    val assignment_suffix = Choice(this, {
        Build(this, 1, Seq(this, {`=`() && {expr()}() }), {Assign(it(0), it(1), "=")})() ||
                Build(this, 1, Seq(this, {`+=`() && {expr()}() }), {Assign(it(0), it(1), "+=")})() ||
                Build(this, 1, Seq(this, {`-=`() && {expr()}() }), {Assign(it(0), it(1), "-=")})() ||
                Build(this, 1, Seq(this, {`*=`() && {expr()}() }), {Assign(it(0), it(1), "*=")})() ||
                Build(this, 1, Seq(this, {dive() && {expr()}() }), {Assign(it(0), it(1), "/=")})() ||
                Build(this, 1, Seq(this, {`%=`() && {expr()}() }), {Assign(it(0), it(1), "%=")})() ||
                Build(this, 1, Seq(this, {sle() && {expr()}() }), {Assign(it(0), it(1), "<<=")})() ||
                Build(this, 1, Seq(this, {sre() && {expr()}() }), {Assign(it(0), it(1), ">>=")})() ||
                Build(this, 1, Seq(this, {bsre() && {expr()}() }), {Assign(it(0), it(1), ">>>=")})() ||
                Build(this, 1, Seq(this, {`&=`() && {expr()}() }), {Assign(it(0), it(1), "&=")})() ||
                Build(this, 1, Seq(this, {`^=`() && {expr()}() }), {Assign(it(0), it(1), "^=")})() ||
                Build(this, 1, Seq(this, {`|=`() && {expr()}() }), {Assign(it(0), it(1), "|=")})()})

    val assignment = Seq(this, {ternary() && Opt(this, assignment_suffix)() })

    val expr : NaiveParser = Choice(this, {lambda() || assignment()})

    /// STATEMENTS =================================================================================

    val if_stmt = Build(this,
            0,
            syntax = Seq(this, {`if`() && par_expr() && {stmt()}() && Maybe(this, Seq(this, {`else`() && {stmt()}() }))() }),
            effect = {If(it(0), it(1), it(2))})

    val expr_stmt_list = Build(this,
            0,
            syntax = CommaList0(this, expr),
            effect = {it.list<Stmt>()})

    val for_init_decl = Build(this,
            0,
            syntax = Seq(this, {modifiers() && var_decl_no_semi() }),
            effect = {it.list<Stmt>()})

    val for_init = Choice(this, {for_init_decl() || expr_stmt_list()})

    val basic_for_paren_part = Seq(this, {for_init() && semi() && Maybe(this, expr)() && semi() && Opt(this, expr_stmt_list)() })

    val basic_for_stmt = Build(this,
            0,
            syntax = Seq(this, {`for`() && Parens(this, basic_for_paren_part)() && {stmt()}() }),
            effect = {BasicFor(it(0), it(1), it(2), it(3))})

    val for_val_decl = Seq(this, {modifiers() && type() && var_declarator_id() && colon() && expr() })

    val enhanced_for_stmt = Build(this,
            0,
            syntax = Seq(this, {`for`() && Parens(this, for_val_decl)() && {stmt()}() }),
            effect = {EnhancedFor(it(0), it(1), it(2), it(3), it(4))})

    val while_stmt = Build(this,
            0,
            syntax = Seq(this, {`while`() && par_expr() && {stmt()}() }),
            effect = {WhileStmt(it(0), it(1))})

    val do_while_stmt = Build(this,
            0,
            syntax = Seq(this, {`do`() && {stmt()}() && `while`() && par_expr() && semi() }),
            effect = {DoWhileStmt(it(0), it(1))})

    val catch_parameter_types = Build(this,
            0,
            syntax = Around0(this, type, `|`),
            effect = {it.list<Type>()})

    val catch_parameter = Seq(this, {modifiers() && catch_parameter_types() && var_declarator_id() })

    val catch_clause = Build(this,
            0,
            syntax = Seq(this, {catch() && Parens(this, catch_parameter)() && {block()}() }),
            effect = {CatchClause(it(0), it(1), it(2), it(3))})

    val catch_clauses = Build(this,
            0,
            syntax = Repeat0(this, catch_clause),
            effect = {it.list<CatchClause>()})

    val finally_clause = Seq(this, {finally() && {block()}() })

    val resource = Build(this,
            0,
            syntax = Seq(this, {modifiers() && type() && var_declarator_id() && `=`() && expr() }),
            effect = {TryResource(it(0), it(1), it(2), it(3))})

    val resources = Build(this,
            0,
            syntax = Opt(this, Parens(this, Around1(this, resource, semi))),
            effect = {it.list<TryResource>()})

    val try_stmt = Build(this,
            0,
            syntax = Seq(this, {`try`() && resources() && {block()}() && catch_clauses() && Maybe(this, finally_clause)() }),
            effect = {TryStmt(it(0), it(1), it(2), it(3))})

    val default_label = Build(this,
            0,
            syntax = Seq(this, {default() && colon() }),
            effect = {DefaultLabel})

    val case_label = Build(this,
            0,
            syntax = Seq(this, {case() && expr() && colon() }),
            effect = {CaseLabel(it(0))})

    val switch_label = Choice(this, {case_label() || default_label()})

    val switch_clause = Build(this,
            0,
            syntax = Seq(this, {switch_label() && {stmts()}() }),
            effect = {SwitchClause(it(0), it(1))})

    val switch_stmt = Build(this,
            0,
            syntax = Seq(this, {switch() && par_expr() && Curlies(this, Repeat0(this, switch_clause))() }),
            effect = {SwitchStmt(it(0), it.list(1))})

    val synchronized_stmt = Build(this,
            0,
            syntax = Seq(this, {synchronized() && par_expr() && {block()}() }),
            effect = {SynchronizedStmt(it(1), it(2))})

    val return_stmt = Build(this,
            0,
            syntax = Seq(this, {`return`() && Maybe(this, expr)() && semi() }),
            effect = {ReturnStmt(it(0))})

    val throw_stmt = Build(this,
            0,
            syntax = Seq(this, {`throw`() && expr() && semi() }),
            effect = {ThrowStmt(it(0))})

    val break_stmt = Build(this,
            0,
            syntax = Seq(this, {`break`() && Maybe(this, iden)() && semi() }),
            effect = {BreakStmt(it(0))})

    val continue_stmt = Build(this,
            0,
            syntax = Seq(this, {`continue`() && Maybe(this, iden)() && semi() }),
            effect = {ContinueStmt(it(0))})

    val assert_stmt = Build(this,
            0,
            syntax = Seq(this, {assert() && expr() && Maybe(this, Seq(this, {colon() && expr() }))() && semi() }),
            effect = {AssertStmt(it(0), it(1))})

    val semi_stmt = Build(this,
            0,
            syntax = semi,
            effect = {SemiStmt})

    val expr_stmt = Seq(this, {expr() && semi() })

    val labelled_stmt = Build(this,
            0,
            syntax = Seq(this, {iden() && colon() && {stmt()}() }),
            effect = {LabelledStmt(it(0), it(1))})

    val stmt : NaiveParser = Choice(this, {{block()}() || if_stmt() || basic_for_stmt() || enhanced_for_stmt() || while_stmt() || do_while_stmt() || try_stmt() || switch_stmt() || synchronized_stmt() || return_stmt() || throw_stmt() || break_stmt() || continue_stmt() || assert_stmt() || semi_stmt() || expr_stmt() || labelled_stmt() || var_decl() || type_decl()})

    val block = Build(this,
            0,
            syntax = Curlies(this, Repeat0(this, stmt)),
            effect = {Block(it.list())})

    val stmts = Build(this,
            0,
            syntax = Repeat0(this, stmt),
            effect = {it.list<Stmt>()})

    /// TOP-LEVEL ==================================================================================

    val package_decl = Build(this,
            0,
            syntax = Seq(this, {annotations() && `package`() && qualified_iden() && semi() }),
            effect = {Package(it(0), it(1))})

    val import_decl = Build(this,
            0,
            syntax = Seq(this, {import() && AsBool(this, static)() && qualified_iden() && AsBool(this, Seq(this, {dot() && `*`() }))() && semi() }),
            effect = {Import(it(0), it(1), it(2))})

    val import_decls = Build(this,
            0,
            syntax = Repeat0(this, import_decl),
            effect = {it.list<Import>()})

    val root = Build(this,
            0,
            syntax = Seq(this, {{whitespace()}() && Maybe(this, package_decl)() && import_decls() && type_decls() }),
            effect = {File(it(0), it(1), it(2))})

    override fun whitespace() = whitespace.invoke()

    override fun root() = root.invoke()
}
