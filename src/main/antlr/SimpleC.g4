grammar SimpleC;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////// PARSER RULES ////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

start : statement+ eof ; // root rule
statement : function_statement | if_statement | variable_declaration ';' | print ';' | for_statement | while_statement ;
function_statement: type IDENTIFIER '(' (function_argument ','?)+ ')' '{' (statement+)? return_statement?'}' | type IDENTIFIER '('')' '{' (statement+)? return_statement? '}';
function_argument: type IDENTIFIER table?;
return_statement: 'return ' (variable_declaration | IDENTIFIER) ';';
for_statement : FOR '(' for_conditions ')' '{' if_or_loop_body '}' ;
if_statement :  IF '(' boolean_expression ')' '{' trueStatement=if_or_loop_body '}' (ELSE '{' falseStatement=if_or_loop_body '}')? ;
while_statement : WHILE '(' boolean_expression  ')' '{' if_or_loop_body '}' ;
if_or_loop_body : statement* ;
print : PRINT LEFT_ROUND_BRACKET string RIGHT_ROUND_BRACKET  ;
string : STRING_LITERAL ;
type : BOOL | STRING | CHAR | INT | FLOAT |VOID ;
variable_declaration
    : assignment
    | type assignment
    | type IDENTIFIER
    | type IDENTIFIER table
    ;
assignment : IDENTIFIER ASSIGN literal | IDENTIFIER ASSIGN expression;
table : '[' INTEGER_LITERAL ']';
for_conditions
    : IDENTIFIER '=' INTEGER_LITERAL ';' IDENTIFIER comparison INTEGER_LITERAL ';' IDENTIFIER INC | DEC ';'   // ex. for(a = 0 : 10)
    ;
boolean_expression
    : expression comparison expression      // ex. a+b > c+d
    | expression comparison primary_expression      // ex. a+b > 0
    | primary_expression comparison primary_expression      // ex. a < b
    | expression logic_operator expression
    | primary_expression logic_operator primary_expression
    | primary_expression
    ;

comparison : GT | LT | LE | GE | EQUALS | NOT_EQUALS ;
logic_operator : AND | OR ;

expression
    : primary_expression math_operator primary_expression ;

primary_expression : IDENTIFIER | FLOATING_POINT_LITERAL | INTEGER_LITERAL ;

math_operator : ADD | SUB | MUL | DIV | MOD ;

literal : INTEGER_LITERAL | FLOATING_POINT_LITERAL | STRING_LITERAL | BOOL_LITERAL ;
eof : EOF ;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////// LEXER RULES (tokens) ////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

PRINT : 'print' ;
ASSIGN : '=' ;
BOOL : 'bool' ;
BREAK : 'break' ;
CASE : 'case' ;
CHAR : 'char' ;
CONTINUE : 'continue' ;
ELSE : 'else' ;
FOR : 'for' ;
WHILE : 'while' ;
IF : 'if' ;
INT : 'int' ;
VOID: 'void' ;
FLOAT : 'float' ;
RETURN : 'return' ;
STRING : 'string';
SWITCH : 'switch' ;
LEFT_ROUND_BRACKET : '(' ;
RIGHT_ROUND_BRACKET : ')' ;
LEFT_BRACE : '{' ;
RIGHT_BRACE : '}' ;
LEFT_SQUARE_BRACKET : '[' ;
RIGHT_SQUARE_BRACKET : ']' ;
COMMA : ',' ;
COLON : ':' ;

// Boolean
GT : '>' ;
LT : '<' ;
NOT : 'not' ;
EQUALS : 'equals' ;
LE : '<=' ;
GE : '>=' ;
NOT_EQUALS : 'not equals' ;
AND : 'and' ;
OR : 'or' ;

// Math
ADD : '+' ;
SUB : '-' ;
MUL : '*' ;
DIV : '/' ;
MOD : '%' ;

// Identifiers
IDENTIFIER : LETTER LETTER_OR_DIGIT*  ;
fragment LETTER : [a-zA-Z_] ;
fragment LETTER_OR_DIGIT : [a-zA-Z0-9_] ;

//constants
fragment QUOTE : '"' ;
fragment MINUS : '-' ;
fragment FLOATING_POINT_SEPARATOR : '.' ;

// Literals
INTEGER_LITERAL : MINUS? DECIMAL_NUMERAL ;
FLOATING_POINT_LITERAL : MINUS? DECIMAL_NUMERAL FLOATING_POINT_SEPARATOR DECIMAL_NUMERAL? ;
NUMBER_LITERAL :INTEGER_LITERAL | FLOATING_POINT_LITERAL ;
STRING_LITERAL : QUOTE ~('\n' | '\r' | '"')* QUOTE ;
BOOL_LITERAL :  'true' | 'false' ;
INC: '++' ;
DEC: '--';

fragment DEC_DIGIT : [0-9] ;
fragment DECIMAL_NUMERAL
   : '0'
   | [1-9] DEC_DIGIT* ;

// Whitespace and comments
WS : [ \t\r\n\u000C]+ -> skip ;
NEWLINE : [\r\n]   -> skip ;
COMMENT : '#' .*?  -> channel(HIDDEN) ;
