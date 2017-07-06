package norswap.lang.java8
import norswap.autumn.TokenGrammar
import norswap.autumn.naive.*
import norswap.autumn.naive.CharRange
import norswap.autumn.naive.Not
import norswap.autumn.model.graph_compiler.set_refs
import norswap.lang.java_base.*
import norswap.lang.java8.ast.*
import norswap.lang.java8.ast.TypeDeclKind.*

class GraphGrammar: TokenGrammar()
{
    val Parser.g: Parser get() = apply { grammar = this@GraphGrammar }
    fun Parser.d(name: String): Parser {
     this.rule = name     
     return this     
    }

    val refs = ArrayList<ReferenceParser>()

    fun ref (name: String) = ReferenceParser(name).also { refs.add(it) }

    /// LEXICAL ====================================================================================

    // Whitespace -------------------------------------------------------------

    val line_comment = Seq(Str("//").g, Until0(CharAny().g, Str("\n").g).g).g.d("line_comment")

    val multi_comment = Seq(Str("/*").g, Until0(CharAny().g, Str("*/").g).g).g.d("multi_comment")

    val whitespace = Repeat0(Choice(SpaceChar().g, line_comment, multi_comment).g).g.d("whitespace")

    // Keywords and Operators -------------------------------------------------

    val boolean = Token("boolean".token).g.d("boolean")

    val byte = Token("byte".token).g.d("byte")

    val char = Token("char".token).g.d("char")

    val double = Token("double".token).g.d("double")

    val float = Token("float".token).g.d("float")

    val int = Token("int".token).g.d("int")

    val long = Token("long".token).g.d("long")

    val short = Token("short".token).g.d("short")

    val void = Token("void".token).g.d("void")

    val abstract = Token("abstract".token).g.d("abstract")

    val default = Token("default".token).g.d("default")

    val final = Token("final".token).g.d("final")

    val native = Token("native".token).g.d("native")

    val private = Token("private".token).g.d("private")

    val protected = Token("protected".token).g.d("protected")

    val public = Token("public".token).g.d("public")

    val static = Token("static".token).g.d("static")

    val strictfp = Token("strictfp".token).g.d("strictfp")

    val synchronized = Token("synchronized".token).g.d("synchronized")

    val transient = Token("transient".token).g.d("transient")

    val volatile = Token("volatile".token).g.d("volatile")

    val `false` = Token(token ({ false }, Str("false").g)).g.d("`false`")

    val `true` = Token(token ({ true }, Str("true").g)).g.d("`true`")

    val `null` = Token(token ({ Null }, Str("null").g)).g.d("`null`")

    val assert = Token("assert".keyword).g.d("assert")

    val `break` = Token("break".keyword).g.d("`break`")

    val case = Token("case".keyword).g.d("case")

    val catch = Token("catch".keyword).g.d("catch")

    val `class` = Token("class".keyword).g.d("`class`")

    val const = Token("const".keyword).g.d("const")

    val `continue` = Token("continue".keyword).g.d("`continue`")

    val `do` = Token("do".keyword).g.d("`do`")

    val `else` = Token("else".keyword).g.d("`else`")

    val enum = Token("enum".keyword).g.d("enum")

    val extends = Token("extends".keyword).g.d("extends")

    val finally = Token("finally".keyword).g.d("finally")

    val `for` = Token("for".keyword).g.d("`for`")

    val goto = Token("goto".keyword).g.d("goto")

    val `if` = Token("if".keyword).g.d("`if`")

    val implements = Token("implements".keyword).g.d("implements")

    val import = Token("import".keyword).g.d("import")

    val `interface` = Token("interface".keyword).g.d("`interface`")

    val instanceof = Token("instanceof".keyword).g.d("instanceof")

    val new = Token("new".keyword).g.d("new")

    val `package` = Token("package".keyword).g.d("`package`")

    val `return` = Token("return".keyword).g.d("`return`")

    val `super` = Token("super".keyword).g.d("`super`")

    val switch = Token("switch".keyword).g.d("switch")

    val `this` = Token("this".keyword).g.d("`this`")

    val throws = Token("throws".keyword).g.d("throws")

    val `throw` = Token("throw".keyword).g.d("`throw`")

    val `try` = Token("try".keyword).g.d("`try`")

    val `while` = Token("while".keyword).g.d("`while`")

    val `!` = Token("!".keyword).g.d("`!`")

    val `%` = Token("%".keyword).g.d("`%`")

    val `%=` = Token("%=".keyword).g.d("`%=`")

    val `&` = Token("&".keyword).g.d("`&`")

    val `&&` = Token("&&".keyword).g.d("`&&`")

    val `&=` = Token("&=".keyword).g.d("`&=`")

    val `(` = Token("(".keyword).g.d("`(`")

    val `)` = Token(")".keyword).g.d("`)`")

    val `*` = Token("*".keyword).g.d("`*`")

    val `*=` = Token("*=".keyword).g.d("`*=`")

    val `+` = Token("+".keyword).g.d("`+`")

    val `++` = Token("++".keyword).g.d("`++`")

    val `+=` = Token("+=".keyword).g.d("`+=`")

    val `,` = Token(",".keyword).g.d("`,`")

    val `-` = Token("-".keyword).g.d("`-`")

    val `--` = Token("--".keyword).g.d("`--`")

    val `-=` = Token("-=".keyword).g.d("`-=`")

    val `=` = Token("=".keyword).g.d("`=`")

    val `==` = Token("==".keyword).g.d("`==`")

    val `?` = Token("?".keyword).g.d("`?`")

    val `^` = Token("^".keyword).g.d("`^`")

    val `^=` = Token("^=".keyword).g.d("`^=`")

    val `{` = Token("{".keyword).g.d("`{`")

    val `|` = Token("|".keyword).g.d("`|`")

    val `|=` = Token("|=".keyword).g.d("`|=`")

    val `!=` = Token("!=".keyword).g.d("`!=`")

    val `||` = Token("||".keyword).g.d("`||`")

    val `}` = Token("}".keyword).g.d("`}`")

    val `~` = Token("~".keyword).g.d("`~`")

    val `@` = Token("@".keyword).g.d("`@`")

    val div = Token("/".keyword).g.d("div")

    val dive = Token("/=".keyword).g.d("dive")

    val gt = Token(">".keyword).g.d("gt")

    val lt = Token("<".keyword).g.d("lt")

    val ge = Token(">=".keyword).g.d("ge")

    val le = Token("<=".keyword).g.d("le")

    val sl = Token("<<".keyword).g.d("sl")

    val sle = Token("<<=".keyword).g.d("sle")

    val sr = WordString(">>").g.d("sr")

    val sre = Token(">>=".keyword).g.d("sre")

    val bsr = WordString(">>>").g.d("bsr")

    val bsre = Token(">>>=".keyword).g.d("bsre")

    val lsbra = Token("[".keyword).g.d("lsbra")

    val rsbra = Token("]".keyword).g.d("rsbra")

    val arrow = Token("->".keyword).g.d("arrow")

    val colon = Token(":".keyword).g.d("colon")

    val semi = Token(";".keyword).g.d("semi")

    val dot = Token(".".keyword).g.d("dot")

    val ellipsis = Token("...".keyword).g.d("ellipsis")

    val dcolon = Token("::".keyword).g.d("dcolon")

    // Identifiers ------------------------------------------------------------

    val iden = Token(token({ it }, JavaIden().g)).g.d("iden")

    val `_` = Str("_").g.d("`_`")

    val dlit = Str(".").g.d("dlit")

    val hex_prefix = Choice(Str("0x").g, Str("0x").g).g.d("hex_prefix")

    val underscores = Repeat0(`_`).g.d("underscores")

    val digits1 = Around1(Digit().g, underscores).g.d("digits1")

    val digits0 = Around0(Digit().g, underscores).g.d("digits0")

    val hex_digits = Around1(HexDigit().g, underscores).g.d("hex_digits")

    val hex_num = Seq(hex_prefix, hex_digits).g.d("hex_num")

    // Numerals - Floating Point ----------------------------------------------

    val hex_significand = Choice(Seq(hex_prefix, Opt(hex_digits).g, dlit, hex_digits).g, Seq(hex_num, Opt(dlit).g).g).g.d("hex_significand")

    val exp_sign_opt = Opt(CharSet("+-").g).g.d("exp_sign_opt")

    val exponent = Seq(CharSet("eE").g, exp_sign_opt, digits1).g.d("exponent")

    val binary_exponent = Seq(CharSet("pP").g, exp_sign_opt, digits1).g.d("binary_exponent")

    val float_suffix = CharSet("fFdD").g.d("float_suffix")

    val float_suffix_opt = Opt(float_suffix).g.d("float_suffix_opt")

    val hex_float_lit = Seq(hex_significand, binary_exponent, float_suffix_opt).g.d("hex_float_lit")

    val decimal_float_lit = Choice(Seq(digits1, dlit, digits0, Opt(exponent).g, float_suffix_opt).g, Seq(dlit, digits1, Opt(exponent).g, float_suffix_opt).g, Seq(digits1, exponent, float_suffix_opt).g, Seq(digits1, Opt(exponent).g, float_suffix).g).g.d("decimal_float_lit")

    val float_literal = Token(token ({ parse_float(it) }, Choice(hex_float_lit, decimal_float_lit).g)).g.d("float_literal")

    // Numerals - Integral ----------------------------------------------------

    val bit = CharSet("01").g.d("bit")

    val binary_prefix = Choice(Str("0b").g, Str("0B").g).g.d("binary_prefix")

    val binary_num = Seq(binary_prefix, Around1(Repeat1(bit).g, underscores).g).g.d("binary_num")

    val octal_num = Seq(Str("0").g, Repeat1(Seq(underscores, OctalDigit().g).g).g).g.d("octal_num")

    val decimal_num = Choice(Str("0").g, digits1).g.d("decimal_num")

    val integer_num = Choice(hex_num, binary_num, octal_num, decimal_num).g.d("integer_num")

    val integer_literal = Token(token ({ parse_int(it) }, Seq(integer_num, Opt(CharSet("lL").g).g).g)).g.d("integer_literal")

    // Characters and Strings -------------------------------------------------

    val octal_escape = Choice(Seq(CharRange('0', '3').g, OctalDigit().g, OctalDigit().g).g, Seq(OctalDigit().g, Opt(OctalDigit().g).g).g).g.d("octal_escape")

    val unicode_escape = Seq(Repeat1(Str("u").g).g, Repeat(4, HexDigit().g ).g).g.d("unicode_escape")

    val escape = Seq(Str("\\").g, Choice(CharSet("btnfr\"'\\").g, octal_escape, unicode_escape).g).g.d("escape")

    val naked_char = Choice(escape, Seq(Not(CharSet("'\\\n\r").g).g, CharAny().g).g).g.d("naked_char")

    val char_literal = Token(token ({ parse_char(it) }, Seq(Str("'").g, naked_char, Str("'").g).g)).g.d("char_literal")

    val naked_string_char = Choice(escape, Seq(Not(CharSet("\"\\\n\r").g).g, CharAny().g).g).g.d("naked_string_char")

    val string_literal = Token(token ({ parse_string(it) }, Seq(Str("\"").g, Repeat0(naked_string_char).g, Str("\"").g).g)).g.d("string_literal")

    // Literal ----------------------------------------------------------------

    val literal_syntax = TokenChoice(this, integer_literal, string_literal, `null`, float_literal, `true`, `false`, char_literal).g.d("literal_syntax")

    val literal = Build(
        syntax = literal_syntax,
        effect = {Literal(it(0))}).g.d("literal")

    /// ANNOTATIONS ================================================================================

    val annotation_element: Parser = Choice(ref("ternary").g, ref("annotation_element_list").g, ref("annotation").g).g.d("annotation_element")

    val annotation_inner_list = CommaListTerm0(ref("annotation_element").g).g.d("annotation_inner_list")

    val annotation_element_list = Build(
        syntax = Curlies(annotation_inner_list).g,
        effect = {AnnotationElementList(it.list())}).g.d("annotation_element_list")

    val annotation_element_pair = Build(
        syntax = Seq(iden, `=`, annotation_element).g,
        effect = {Pair<String, AnnotationElement>(it(0), it(1))}).g.d("annotation_element_pair")

    val normal_annotation_suffix = Build(1,
        syntax = Parens(CommaList1(annotation_element_pair).g).g,
        effect = {NormalAnnotation(it(0), it.list<Pair<String, AnnotationElement>>(1))}).g.d("normal_annotation_suffix")

    val single_element_annotation_suffix = Build(1,
        syntax = Parens(annotation_element).g,
        effect = {SingleElementAnnotation(it(0), it(1))}).g.d("single_element_annotation_suffix")

    val marker_annotation_suffix = Build(1,
        syntax = Opt(ParensEmpty().g).g,
        effect = {MarkerAnnotation(it(0))}).g.d("marker_annotation_suffix")

    val annotation_suffix = Choice(normal_annotation_suffix, single_element_annotation_suffix, marker_annotation_suffix).g.d("annotation_suffix")

    val qualified_iden = Build(
        syntax = Around1(iden, dot).g,
        effect = {it.list<String>()}).g.d("qualified_iden")

    val annotation = Seq(`@`, qualified_iden, annotation_suffix).g.d("annotation")

    val annotations = Build(
        syntax = Repeat0(annotation).g,
        effect = {it.list<Annotation>()}).g.d("annotations")

    /// TYPES ======================================================================================

    val basic_type = TokenChoice(this, byte, short, int, long, char, float, double, boolean, void).g.d("basic_type")

    val primitive_type = Build(
        syntax = Seq(annotations, basic_type).g,
        effect = {PrimitiveType(it(0), it(1))}).g.d("primitive_type")

    val extends_bound = Build(
        syntax = Seq(extends, ref("type").g).g,
        effect = {ExtendsBound(it(0))}).g.d("extends_bound")

    val super_bound = Build(
        syntax = Seq(`super`, ref("type").g).g,
        effect = {SuperBound(it(0))}).g.d("super_bound")

    val type_bound = Maybe(Choice(extends_bound, super_bound).g).g.d("type_bound")

    val wildcard = Build(
        syntax = Seq(annotations, `?`, type_bound).g,
        effect = {Wildcard(it(0), it(1))}).g.d("wildcard")

    val type_args = Build(
        syntax = Opt(Angles(CommaList0(Choice(ref("type").g, wildcard).g).g).g).g,
        effect = {it.list<Type>()}).g.d("type_args")

    val class_type_part = Build(
        syntax = Seq(annotations, iden, type_args).g,
        effect = {ClassTypePart(it(0), it(1), it(2))}).g.d("class_type_part")

    val class_type = Build(
        syntax = Around1(class_type_part, dot).g,
        effect = {ClassType(it.list<ClassTypePart>())}).g.d("class_type")

    val stem_type = Choice(primitive_type, class_type).g.d("stem_type")

    val dim = Build(
        syntax = Seq(annotations, SquaresEmpty().g).g,
        effect = {Dimension(it(0))}).g.d("dim")

    val dims = Build(
        syntax = Repeat0(dim).g,
        effect = {it.list<Dimension>()}).g.d("dims")

    val dims1 = Build(
        syntax = Repeat1(dim).g,
        effect = {it.list<Dimension>()}).g.d("dims1")

    val type_dim_suffix = Build(1,
        syntax = dims1,
        effect = {ArrayType(it(0), it(1))}).g.d("type_dim_suffix")

    val type: Parser = Seq(stem_type, Opt(type_dim_suffix).g).g.d("type")

    val type_union_syntax = Around1(ref("type").g, `&`).g.d("type_union_syntax")

    val type_union = Build(
        syntax = type_union_syntax,
        effect = {it.list<Type>()}).g.d("type_union")

    val type_bounds = Build(
        syntax = Opt(Seq(extends, type_union_syntax).g).g,
        effect = {it.list<Type>()}).g.d("type_bounds")

    val type_param = Build(
        syntax = Seq(annotations, iden, type_bounds).g,
        effect = {TypeParam(it(0), it(1), it(2))}).g.d("type_param")

    val type_params = Build(
        syntax = Opt(Angles(CommaList0(type_param).g).g).g,
        effect = {it.list<TypeParam>()}).g.d("type_params")

    /// MODIFIERS ==================================================================================

    val keyword_modifier = Build(
        syntax = Choice(public, protected, private, abstract, static, final, synchronized, native, strictfp, default, transient, volatile).g,
        effect = {Keyword.valueOf(it(0))}).g.d("keyword_modifier")

    val modifier = Choice(annotation, keyword_modifier).g.d("modifier")

    val modifiers = Build(
        syntax = Repeat0(modifier).g,
        effect = {it.list<Modifier>()}).g.d("modifiers")

    /// PARAMETERS =================================================================================

    val args = Build(
        syntax = Parens(CommaList0(ref("expr").g).g).g,
        effect = {it.list<Expr>()}).g.d("args")

    val this_parameter_qualifier = Build(
        syntax = Repeat0(Seq(iden, dot).g).g,
        effect = {it.list<String>()}).g.d("this_parameter_qualifier")

    val this_param_suffix = Build(2,
        syntax = Seq(this_parameter_qualifier, `this`).g,
        effect = {ThisParameter(it(0), it(1), it(2))}).g.d("this_param_suffix")

    val iden_param_suffix = Build(2,
        syntax = Seq(iden, dims).g,
        effect = {IdenParameter(it(0), it(1), it(2), it(3))}).g.d("iden_param_suffix")

    val variadic_param_suffix = Build(2,
        syntax = Seq(annotations, ellipsis, iden).g,
        effect = {VariadicParameter(it(0), it(1), it(2), it(3))}).g.d("variadic_param_suffix")

    val formal_param_suffix = Choice(iden_param_suffix, this_param_suffix, variadic_param_suffix).g.d("formal_param_suffix")

    val formal_param = Seq(modifiers, type, formal_param_suffix).g.d("formal_param")

    val formal_params = Build(
        syntax = Parens(CommaList0(formal_param).g).g,
        effect = {FormalParameters(it.list())}).g.d("formal_params")

    val untyped_params = Build(
        syntax = Parens(CommaList1(iden).g).g,
        effect = {UntypedParameters(it.list())}).g.d("untyped_params")

    val single_param = Build(
        syntax = iden,
        effect = {UntypedParameters(it.list<String>())}).g.d("single_param")

    val lambda_params = Choice(formal_params, untyped_params, single_param).g.d("lambda_params")

    /// NON-TYPE DECLARATIONS ======================================================================

    val var_init: Parser = Choice(ref("expr").g, ref("array_init").g).g.d("var_init")

    val array_init = Build(
        syntax = Curlies(CommaListTerm0(var_init).g).g,
        effect = {ArrayInit(it.list())}).g.d("array_init")

    val var_declarator_id = Build(
        syntax = Seq(iden, dims).g,
        effect = {VarDeclaratorID(it(0), it(1))}).g.d("var_declarator_id")

    val var_declarator = Build(
        syntax = Seq(var_declarator_id, Maybe(Seq(`=`, var_init).g).g).g,
        effect = {VarDeclarator(it(0), it(1))}).g.d("var_declarator")

    val var_decl_no_semi = Build(1,
        syntax = Seq(type, CommaList1(var_declarator).g).g,
        effect = {VarDecl(it(0), it(1), it.list(2))}).g.d("var_decl_no_semi")

    val var_decl_suffix = Seq(var_decl_no_semi, semi).g.d("var_decl_suffix")

    val var_decl = Seq(modifiers, var_decl_suffix).g.d("var_decl")

    val throws_clause = Build(
        syntax = Opt(Seq(throws, CommaList1(type).g).g).g,
        effect = {it.list<Type>()}).g.d("throws_clause")

    val block_or_semi = Choice(ref("block").g, AsVal(null, semi ).g).g.d("block_or_semi")

    val method_decl_suffix = Build(1,
        syntax = Seq(type_params, type, iden, formal_params, dims, throws_clause, block_or_semi).g,
        effect = {MethodDecl(it(0), it(1), it(2), it(3), it(4), it(5), it(6), it(7))}).g.d("method_decl_suffix")

    val constructor_decl_suffix = Build(1,
        syntax = Seq(type_params, iden, formal_params, throws_clause, ref("block").g).g,
        effect = {ConstructorDecl(it(0), it(1), it(2), it(3), it(4), it(5))}).g.d("constructor_decl_suffix")

    val init_block = Build(
        syntax = Seq(AsBool(static).g, ref("block").g).g,
        effect = {InitBlock(it(0), it(1))}).g.d("init_block")

    /// TYPE DECLARATIONS ==========================================================================

    // Common -----------------------------------------------------------------

    val extends_clause = Build(
        syntax = Opt(Seq(extends, CommaList0(type).g).g).g,
        effect = {it.list<Type>()}).g.d("extends_clause")

    val implements_clause = Build(
        syntax = Opt(Seq(implements, CommaList0(type).g).g).g,
        effect = {it.list<Type>()}).g.d("implements_clause")

    val type_sig = Seq(iden, type_params, extends_clause, implements_clause).g.d("type_sig")

    val class_modified_decl = Seq(modifiers, Choice(var_decl_suffix, method_decl_suffix, constructor_decl_suffix, ref("type_decl_suffix").g).g).g.d("class_modified_decl")

    val class_body_decl: Parser = Choice(class_modified_decl, init_block, semi).g.d("class_body_decl")

    val class_body_decls = Build(
        syntax = Repeat0(class_body_decl).g,
        effect = {it.list<Decl>()}).g.d("class_body_decls")

    val type_body = Curlies(class_body_decls).g.d("type_body")

    // Enum -------------------------------------------------------------------

    val enum_constant = Build(
        syntax = Seq(annotations, iden, Maybe(args).g, Maybe(type_body).g).g,
        effect = {EnumConstant(it(0), it(1), it(2), it(3))}).g.d("enum_constant")

    val enum_class_decls = Build(
        syntax = Opt(Seq(semi, Repeat0(class_body_decl).g).g).g,
        effect = {it.list<Decl>()}).g.d("enum_class_decls")

    val enum_constants = Build(
        syntax = Opt(CommaList1(enum_constant).g).g,
        effect = {it.list<EnumConstant>()}).g.d("enum_constants")

    val enum_body = Affect(
        syntax = Curlies(Seq(enum_constants, enum_class_decls).g).g,
        effect = { stack.push(it(1)) ; stack.push(it(0)) /* swap */ }).g.d("enum_body")

    val enum_decl = Build(1,
        syntax = Seq(enum, type_sig, enum_body).g,
        effect = {val td = TypeDecl(input, ENUM, it(0), it(1), it(2), it(3), it(4), it(5))
                   EnumDecl(td, it(6))}).g.d("enum_decl")

    // Annotations ------------------------------------------------------------

    val annot_default_clause = Build(
        syntax = Seq(default, annotation_element).g,
        effect = {it(1)}).g.d("annot_default_clause")

    val annot_elem_decl = Build(
        syntax = Seq(modifiers, type, iden, ParensEmpty().g, dims, Maybe(annot_default_clause).g, semi).g,
        effect = {AnnotationElemDecl(it(0), it(1), it(2), it(3), it(4))}).g.d("annot_elem_decl")

    val annot_body_decls = Build(
        syntax = Repeat0(Choice(annot_elem_decl, class_body_decl).g).g,
        effect = {it.list<Decl>()}).g.d("annot_body_decls")

    val annotation_decl = Build(1,
        syntax = Seq(`@`, `interface`, type_sig, Curlies(annot_body_decls).g).g,
        effect = {TypeDecl(input, ANNOTATION, it(0), it(1), it(2), it(3), it(4), it(5))}).g.d("annotation_decl")

    //// ------------------------------------------------------------------------

    val class_decl = Build(1,
        syntax = Seq(`class`, type_sig, type_body).g,
        effect = {TypeDecl(input, CLASS, it(0), it(1), it(2), it(3), it(4), it(5))}).g.d("class_decl")

    val interface_declaration = Build(1,
        syntax = Seq(`interface`, type_sig, type_body).g,
        effect = {TypeDecl(input, INTERFACE, it(0), it(1), it(2), it(3), it(4), it(5))}).g.d("interface_declaration")

    val type_decl_suffix = Choice(class_decl, interface_declaration, enum_decl, annotation_decl).g.d("type_decl_suffix")

    val type_decl = Seq(modifiers, type_decl_suffix).g.d("type_decl")

    val type_decls = Build(
        syntax = Repeat0(Choice(type_decl, semi).g).g,
        effect = {it.list<Decl>()}).g.d("type_decls")

    /// EXPRESSIONS ================================================================================

    // Array Constructor ------------------------------------------------------

    val dim_expr = Build(
        syntax = Seq(annotations, Squares(ref("expr").g).g).g,
        effect = {DimExpr(it(0), it(1))}).g.d("dim_expr")

    val dim_exprs = Build(
        syntax = Repeat1(dim_expr).g,
        effect = {it.list<DimExpr>()}).g.d("dim_exprs")

    val dim_expr_array_creator = Build(
        syntax = Seq(stem_type, dim_exprs, dims).g,
        effect = {ArrayCtorCall(it(0), it(1), it(2), null)}).g.d("dim_expr_array_creator")

    val init_array_creator = Build(
        syntax = Seq(stem_type, dims1, array_init).g,
        effect = {ArrayCtorCall(it(0), emptyList(), it(1), it(2))}).g.d("init_array_creator")

    val array_ctor_call = Seq(new, Choice(dim_expr_array_creator, init_array_creator).g).g.d("array_ctor_call")

    // Lambda Expression ------------------------------------------------------

    val lambda = Build(
        syntax = Seq(lambda_params, arrow, Choice(ref("block").g, ref("expr").g).g).g,
        effect = {Lambda(it(0), it(1))}).g.d("lambda")

    // Expression - Primary ---------------------------------------------------

    val par_expr = Build(
        syntax = Parens(ref("expr").g).g,
        effect = {ParenExpr(it(0))}).g.d("par_expr")

    val ctor_call = Build(
        syntax = Seq(new, type_args, stem_type, args, Maybe(type_body).g).g,
        effect = {CtorCall(it(0), it(1), it(2), it(3))}).g.d("ctor_call")

    val new_ref_suffix = Build(2,
        syntax = new,
        effect = {NewReference(it(0), it(1))}).g.d("new_ref_suffix")

    val method_ref_suffix = Build(2,
        syntax = iden,
        effect = {MaybeBoundMethodReference(it(0), it(1), it(2))}).g.d("method_ref_suffix")

    val ref_suffix = Seq(dcolon, type_args, Choice(new_ref_suffix, method_ref_suffix).g).g.d("ref_suffix")

    val class_expr_suffix = Build(1,
        syntax = Seq(dot, `class`).g,
        effect = {ClassExpr(it(0))}).g.d("class_expr_suffix")

    val type_suffix_expr = Seq(type, Choice(ref_suffix, class_expr_suffix).g).g.d("type_suffix_expr")

    val iden_or_method_expr = Build(
        syntax = Seq(iden, Maybe(args).g).g,
        effect = {it[1] ?. let { MethodCall(null, listOf(), it(0), it(1)) } ?: Identifier(it(0))}).g.d("iden_or_method_expr")

    val this_expr = Build(
        syntax = Seq(`this`, Maybe(args).g).g,
        effect = {it[0] ?. let { ThisCall(it(0)) } ?: This}).g.d("this_expr")

    val super_expr = Build(
        syntax = Seq(`super`, Maybe(args).g).g,
        effect = {it[0] ?. let { SuperCall(it(0)) } ?: Super}).g.d("super_expr")

    val class_expr = Build(
        syntax = Seq(type, dot, `class`).g,
        effect = {ClassExpr(it(0))}).g.d("class_expr")

    val primary_expr = Choice(par_expr, array_ctor_call, ctor_call, type_suffix_expr, iden_or_method_expr, this_expr, super_expr, literal).g.d("primary_expr")

    // Expression - Postfix ---------------------------------------------------

    val dot_this = Build(1,
        syntax = `this`,
        effect = {DotThis(it(0))}).g.d("dot_this")

    val dot_super = Build(1,
        syntax = `super`,
        effect = {DotSuper(it(0))}).g.d("dot_super")

    val dot_iden = Build(1,
        syntax = iden,
        effect = {DotIden(it(0), it(1))}).g.d("dot_iden")

    val dot_new = Build(1,
        syntax = ctor_call,
        effect = {DotNew(it(0), it(1))}).g.d("dot_new")

    val dot_method = Build(1,
        syntax = Seq(type_args, iden, args).g,
        effect = {MethodCall(it(0), it(1), it(2), it(3))}).g.d("dot_method")

    val dot_postfix = Choice(dot_method, dot_iden, dot_this, dot_super, dot_new).g.d("dot_postfix")

    val ref_postfix = Build(1,
        syntax = Seq(dcolon, type_args, iden).g,
        effect = {BoundMethodReference(it(0), it(1), it(2))}).g.d("ref_postfix")

    val array_postfix = Build(1,
        syntax = Squares(ref("expr").g).g,
        effect = {ArrayAccess(it(0), it(1))}).g.d("array_postfix")

    val inc_suffix = Build(1,
        syntax = `++`,
        effect = {PostIncrement(it(0))}).g.d("inc_suffix")

    val dec_suffix = Build(1,
        syntax = `--`,
        effect = {PostDecrement(it(0))}).g.d("dec_suffix")

    val postfix = Choice(Seq(dot, dot_postfix).g, array_postfix, inc_suffix, dec_suffix, ref_postfix).g.d("postfix")

    val postfix_expr = Seq(primary_expr, Repeat0(postfix).g).g.d("postfix_expr")

    val inc_prefix = Build(
        syntax = Seq(`++`, ref("prefix_expr").g).g,
        effect = {PreIncrement(it(0))}).g.d("inc_prefix")

    val dec_prefix = Build(
        syntax = Seq(`--`, ref("prefix_expr").g).g,
        effect = {PreDecrement(it(0))}).g.d("dec_prefix")

    val unary_plus = Build(
        syntax = Seq(`+`, ref("prefix_expr").g).g,
        effect = {UnaryPlus(it(0))}).g.d("unary_plus")

    val unary_minus = Build(
        syntax = Seq(`-`, ref("prefix_expr").g).g,
        effect = {UnaryMinus(it(0))}).g.d("unary_minus")

    val complement = Build(
        syntax = Seq(`~`, ref("prefix_expr").g).g,
        effect = {Complement(it(0))}).g.d("complement")

    val not = Build(
        syntax = Seq(`!`, ref("prefix_expr").g).g,
        effect = {Negate(it(0))}).g.d("not")

    val cast = Build(
        syntax = Seq(Parens(type_union).g, Choice(lambda, ref("prefix_expr").g).g).g,
        effect = {Cast(it(0), it(1))}).g.d("cast")

    val prefix_expr: Parser = Choice(inc_prefix, dec_prefix, unary_plus, unary_minus, complement, not, cast, postfix_expr).g.d("prefix_expr")

    // Expression - Binary ----------------------------------------------------

    val mult_expr = AssocLeft(this) { 
        operands = prefix_expr
        op(`*`, { Product(it(0), it(1)) })
        op(div, { Division(it(0), it(1)) })
        op(`%`, { Remainder(it(0), it(1)) })
    }.g.d("mult_expr")

    val add_expr = AssocLeft(this) { 
        operands = mult_expr
        op(`+`, { Sum(it(0), it(1)) })
        op(`-`, { Diff(it(0), it(1)) })
    }.g.d("add_expr")

    val shift_expr = AssocLeft(this) { 
        operands = add_expr
        op(sl, { ShiftLeft(it(0), it(1)) })
        op(sr, { ShiftRight(it(0), it(1)) })
        op(bsr, { BinaryShiftRight(it(0), it(1)) })
    }.g.d("shift_expr")

    val order_expr = AssocLeft(this) { 
        operands = shift_expr
        op(lt, { Lower(it(0), it(1)) })
        op(le, { LowerEqual(it(0), it(1)) })
        op(gt, { Greater(it(0), it(1)) })
        op(ge, { GreaterEqual(it(0), it(1)) })
        postfix(Seq(instanceof, type).g, { Instanceof(it(0), it(1)) })
    }.g.d("order_expr")

    val eq_expr = AssocLeft(this) { 
        operands = order_expr
        op(`==`, { Equal(it(0), it(1)) })
        op(`!=`, { NotEqual(it(0), it(1)) })
    }.g.d("eq_expr")

    val binary_and_expr = AssocLeft(this) { 
        operands = eq_expr
        op(`&`, { BinaryAnd(it(0), it(1)) })
    }.g.d("binary_and_expr")

    val xor_expr = AssocLeft(this) { 
        operands = binary_and_expr
        op(`^`, { Xor(it(0), it(1)) })
    }.g.d("xor_expr")

    val binary_or_expr = AssocLeft(this) { 
        operands = xor_expr
        op(`|`, { BinaryOr(it(0), it(1)) })
    }.g.d("binary_or_expr")

    val and_expr = AssocLeft(this) { 
        operands = binary_or_expr
        op(`&&`, { And(it(0), it(1)) })
    }.g.d("and_expr")

    val or_expr = AssocLeft(this) { 
        operands = and_expr
        op(`||`, { Or(it(0), it(1)) })
    }.g.d("or_expr")

    val ternary_suffix = Build(1,
        syntax = Seq(`?`, ref("expr").g, colon, ref("expr").g).g,
        effect = {Ternary(it(0), it(1), it(2))}).g.d("ternary_suffix")

    val ternary = Seq(or_expr, Opt(ternary_suffix).g).g.d("ternary")

    val assignment_suffix = Choice(
             Build(1,Seq(`=`, ref("expr").g).g, {Assign(it(0), it(1), "=")}).g, 
             Build(1,Seq(`+=`, ref("expr").g).g, {Assign(it(0), it(1), "+=")}).g, 
             Build(1,Seq(`-=`, ref("expr").g).g, {Assign(it(0), it(1), "-=")}).g, 
             Build(1,Seq(`*=`, ref("expr").g).g, {Assign(it(0), it(1), "*=")}).g, 
             Build(1,Seq(dive, ref("expr").g).g, {Assign(it(0), it(1), "/=")}).g, 
             Build(1,Seq(`%=`, ref("expr").g).g, {Assign(it(0), it(1), "%=")}).g, 
             Build(1,Seq(sle, ref("expr").g).g, {Assign(it(0), it(1), "<<=")}).g, 
             Build(1,Seq(sre, ref("expr").g).g, {Assign(it(0), it(1), ">>=")}).g, 
             Build(1,Seq(bsre, ref("expr").g).g, {Assign(it(0), it(1), ">>>=")}).g, 
             Build(1,Seq(`&=`, ref("expr").g).g, {Assign(it(0), it(1), "&=")}).g, 
             Build(1,Seq(`^=`, ref("expr").g).g, {Assign(it(0), it(1), "^=")}).g, 
             Build(1,Seq(`|=`, ref("expr").g).g, {Assign(it(0), it(1), "|=")}).g).g.d("assignment_suffix")

    val assignment = Seq(ternary, Opt(assignment_suffix).g).g.d("assignment")

    val expr: Parser = Choice(lambda, assignment).g.d("expr")

    /// STATEMENTS =================================================================================

    val if_stmt = Build(
        syntax = Seq(`if`, par_expr, ref("stmt").g, Maybe(Seq(`else`, ref("stmt").g).g).g).g,
        effect = {If(it(0), it(1), it(2))}).g.d("if_stmt")

    val expr_stmt_list = Build(
        syntax = CommaList0(expr).g,
        effect = {it.list<Stmt>()}).g.d("expr_stmt_list")

    val for_init_decl = Build(
        syntax = Seq(modifiers, var_decl_no_semi).g,
        effect = {it.list<Stmt>()}).g.d("for_init_decl")

    val for_init = Choice(for_init_decl, expr_stmt_list).g.d("for_init")

    val basic_for_paren_part = Seq(for_init, semi, Maybe(expr).g, semi, Opt(expr_stmt_list).g).g.d("basic_for_paren_part")

    val basic_for_stmt = Build(
        syntax = Seq(`for`, Parens(basic_for_paren_part).g, ref("stmt").g).g,
        effect = {BasicFor(it(0), it(1), it(2), it(3))}).g.d("basic_for_stmt")

    val for_val_decl = Seq(modifiers, type, var_declarator_id, colon, expr).g.d("for_val_decl")

    val enhanced_for_stmt = Build(
        syntax = Seq(`for`, Parens(for_val_decl).g, ref("stmt").g).g,
        effect = {EnhancedFor(it(0), it(1), it(2), it(3), it(4))}).g.d("enhanced_for_stmt")

    val while_stmt = Build(
        syntax = Seq(`while`, par_expr, ref("stmt").g).g,
        effect = {WhileStmt(it(0), it(1))}).g.d("while_stmt")

    val do_while_stmt = Build(
        syntax = Seq(`do`, ref("stmt").g, `while`, par_expr, semi).g,
        effect = {DoWhileStmt(it(0), it(1))}).g.d("do_while_stmt")

    val catch_parameter_types = Build(
        syntax = Around0(type, `|`).g,
        effect = {it.list<Type>()}).g.d("catch_parameter_types")

    val catch_parameter = Seq(modifiers, catch_parameter_types, var_declarator_id).g.d("catch_parameter")

    val catch_clause = Build(
        syntax = Seq(catch, Parens(catch_parameter).g, ref("block").g).g,
        effect = {CatchClause(it(0), it(1), it(2), it(3))}).g.d("catch_clause")

    val catch_clauses = Build(
        syntax = Repeat0(catch_clause).g,
        effect = {it.list<CatchClause>()}).g.d("catch_clauses")

    val finally_clause = Seq(finally, ref("block").g).g.d("finally_clause")

    val resource = Build(
        syntax = Seq(modifiers, type, var_declarator_id, `=`, expr).g,
        effect = {TryResource(it(0), it(1), it(2), it(3))}).g.d("resource")

    val resources = Build(
        syntax = Opt(Parens(Around1(resource, semi).g).g).g,
        effect = {it.list<TryResource>()}).g.d("resources")

    val try_stmt = Build(
        syntax = Seq(`try`, resources, ref("block").g, catch_clauses, Maybe(finally_clause).g).g,
        effect = {TryStmt(it(0), it(1), it(2), it(3))}).g.d("try_stmt")

    val default_label = Build(
        syntax = Seq(default, colon).g,
        effect = {DefaultLabel}).g.d("default_label")

    val case_label = Build(
        syntax = Seq(case, expr, colon).g,
        effect = {CaseLabel(it(0))}).g.d("case_label")

    val switch_label = Choice(case_label, default_label).g.d("switch_label")

    val switch_clause = Build(
        syntax = Seq(switch_label, ref("stmts").g).g,
        effect = {SwitchClause(it(0), it(1))}).g.d("switch_clause")

    val switch_stmt = Build(
        syntax = Seq(switch, par_expr, Curlies(Repeat0(switch_clause).g).g).g,
        effect = {SwitchStmt(it(0), it.list(1))}).g.d("switch_stmt")

    val synchronized_stmt = Build(
        syntax = Seq(synchronized, par_expr, ref("block").g).g,
        effect = {SynchronizedStmt(it(1), it(2))}).g.d("synchronized_stmt")

    val return_stmt = Build(
        syntax = Seq(`return`, Maybe(expr).g, semi).g,
        effect = {ReturnStmt(it(0))}).g.d("return_stmt")

    val throw_stmt = Build(
        syntax = Seq(`throw`, expr, semi).g,
        effect = {ThrowStmt(it(0))}).g.d("throw_stmt")

    val break_stmt = Build(
        syntax = Seq(`break`, Maybe(iden).g, semi).g,
        effect = {BreakStmt(it(0))}).g.d("break_stmt")

    val continue_stmt = Build(
        syntax = Seq(`continue`, Maybe(iden).g, semi).g,
        effect = {ContinueStmt(it(0))}).g.d("continue_stmt")

    val assert_stmt = Build(
        syntax = Seq(assert, expr, Maybe(Seq(colon, expr).g).g, semi).g,
        effect = {AssertStmt(it(0), it(1))}).g.d("assert_stmt")

    val semi_stmt = Build(
        syntax = semi,
        effect = {SemiStmt}).g.d("semi_stmt")

    val expr_stmt = Seq(expr, semi).g.d("expr_stmt")

    val labelled_stmt = Build(
        syntax = Seq(iden, colon, ref("stmt").g).g,
        effect = {LabelledStmt(it(0), it(1))}).g.d("labelled_stmt")

    val stmt: Parser = Choice(ref("block").g, if_stmt, basic_for_stmt, enhanced_for_stmt, while_stmt, do_while_stmt, try_stmt, switch_stmt, synchronized_stmt, return_stmt, throw_stmt, break_stmt, continue_stmt, assert_stmt, semi_stmt, expr_stmt, labelled_stmt, var_decl, type_decl).g.d("stmt")

    val block = Build(
        syntax = Curlies(Repeat0(stmt).g).g,
        effect = {Block(it.list())}).g.d("block")

    val stmts = Build(
        syntax = Repeat0(stmt).g,
        effect = {it.list<Stmt>()}).g.d("stmts")

    /// TOP-LEVEL ==================================================================================

    val package_decl = Build(
        syntax = Seq(annotations, `package`, qualified_iden, semi).g,
        effect = {Package(it(0), it(1))}).g.d("package_decl")

    val import_decl = Build(
        syntax = Seq(import, AsBool(static).g, qualified_iden, AsBool(Seq(dot, `*`).g).g, semi).g,
        effect = {Import(it(0), it(1), it(2))}).g.d("import_decl")

    val import_decls = Build(
        syntax = Repeat0(import_decl).g,
        effect = {it.list<Import>()}).g.d("import_decls")

    val root = Build(
        syntax = Seq(ref("whitespace").g, Maybe(package_decl).g, import_decls, type_decls).g,
        effect = {File(it(0), it(1), it(2))}).g.d("root")

    override fun whitespace() = whitespace.invoke()

    override fun root() = root.invoke()

    init { set_refs(this, refs) }


}

fun main (args: Array<String>) { GraphGrammar() }
