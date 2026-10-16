/**
 * Analisador léxico para a linguagem Jaca
 */
package compiler.scanner;

%%

%public
%class Scanner
%unicode
%type Token

%yylexthrow Exception

%eofval{
    return new Token(Tag.EOF, yyline, yycolumn);
%eofval}

%line
%column

%eof{
    System.out.println("Análise léxica terminada com sucesso!");
%eof}


// Precisa definir macros de expressões regulares
letter = [A-Za-z_]
digit = [0-9]
id = {letter}({letter}|{digit})*
number = {digit}+(\.{digit}+)?([Ee][+-]?{digit}+)?
ws = [ \t\r\n\f]+
add_operator = ("+"|"-")
mul_operator = ("*"|"/"|"%")
rel_operator = ("=="|"!="|"<="|">="|"<"|">")
%%
"if"        { return new Token(Tag.IF, yyline, yycolumn); }
"then"      { return new Token(Tag.THEN, yyline, yycolumn); }
"else"      { return new Token(Tag.ELSE, yyline, yycolumn); }
"while"     { return new Token(Tag.WHILE, yyline, yycolumn); }
{id}        { return new IdToken(yytext(), yyline, yycolumn); }
{number}    { return new NumberToken(yytext(), yyline, yycolumn); }
"="         { return new Token(Tag.ASSIGN, yyline, yycolumn); }
{rel_operator} { return new Token(Tag.REL_OPERATOR, yyline, yycolumn); }
{add_operator} { return new Token(Tag.ADD_OPERATOR, yyline, yycolumn); }
{mul_operator} { return new Token(Tag.MUL_OPERATOR, yyline, yycolumn); }
"("         { return new Token(Tag.LEFT_PAREN, yyline, yycolumn); }
")"         { return new Token(Tag.RIGHT_PAREN, yyline, yycolumn); }
"{"         { return new Token(Tag.LEFT_BRACE, yyline, yycolumn); }
"}"         { return new Token(Tag.RIGHT_BRACE, yyline, yycolumn); }
";"         { return new Token(Tag.SEMICOLON, yyline, yycolumn); }


{ws}		{ /* ignorar */ }
.		        { throw new Exception("Scanner: símbolo ilegal <" +
                     yytext() + "(" + (int)(yytext().charAt(0)) + ")" + ">"); }
