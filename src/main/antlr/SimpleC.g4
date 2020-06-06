grammar SimpleC;

start: IncludeDirective+ function+ eof;

eof: EOF;

IncludeDirective
    :   '#' Whitespace? 'include' Whitespace? (('"' ~[\r\n]* '"') | ('<' ~[\r\n]* '>' )) Whitespace? Newline
        -> skip
    ;

function: type Identifier '(' argument+ ')' '{' statement+ RETURN Whitespace Identifier Whitespace? '}' Newline;

type : BOOL | STRING | CHAR | INT | FLOAT | LONG | DOUBLE | SHORT | VOID;

argument: type Identifier Whitespace?;

statement: variableDeclaration | forStatement | whileStatement | ifStatement | switchStatement | print | variableAssignment;

forStatement: FOR '(' forCondition ')' Newline? '{' Newline statement* '}' Newline;

whileStatement: WHILE '(' booleanExpression ')' Newline? '{' Newline statement* '}' Newline;

ifStatement: IF '(' booleanExpression ')' Newline? '{' Newline statement* '}' Newline (ELSE '{' statement* '}' )?;

switchStatement: SWITCH '(' Identifier ')' Newline? '{' Newline (CASE caseExpression ':' Newline statement?)+ BREAK Newline DEFAULT ':' statement '}';

caseExpression: literal;

print: PRINT '(' string ')';

string: STRING_LITERAL;

variableDeclaration
: type Identifier
| type variableAssignment;

variableAssignment
:Identifier ASSIGN literal
| Identifier ASSIGN expression;

expression: primaryExpression mathOperator primaryExpression;

forCondition: Identifier '=' INT_LITERAL ':' INT_LITERAL;

booleanExpression
: expression comparison expression
| expression comparison primaryExpression
| primaryExpression comparison primaryExpression
| booleanExpression logicOperator booleanExpression
| primaryExpression;

comparison: GT | LT | LE | GE | EQ | NEQ ;

logicOperator: AND | OR;

literal: INT_LITERAL | FLOAT_LITERAL | STRING_LITERAL | BOOL_LITERAL;

primaryExpression: Identifier | literal;

mathOperator: ADD | SUB | MUL | DIV | MOD ;

PRINT : 'print' ;
ASSIGN : '=' ;
BOOL : 'bool' ;
BREAK : 'break' ;
CASE : 'case' ;
DEFAULT: 'default' ;
CHAR : 'char' ;
ELSE : 'else' ;
FOR : 'for' ;
WHILE : 'while' ;
IF : 'if' ;
INT : 'int' ;
FLOAT : 'float' ;
LONG: 'long';
DOUBLE: 'double';
SHORT: 'short';
VOID: 'void';
RETURN : 'return' ;
STRING : 'string';
SWITCH : 'switch' ;
GT : '>' ;
LT : '<' ;
NOT : 'not' ;
EQ : 'equals' ;
LE : '<=' ;
GE : '>=' ;
NEQ : 'not equals' ;
AND : 'and' ;
OR : 'or' ;

// Math
ADD : '+' ;
SUB : '-' ;
MUL : '*' ;
DIV : '/' ;
MOD : '%' ;

Identifier: LETTER LetterOrDigit;

LETTER: [a-zA-Z_] ;
LetterOrDigit: [a-zA-Z0-9_] ;
NUMBER
: '0'
| [1-9][1-9]* ;

FloatSep : '.' ;

INT_LITERAL: '-'? NUMBER;
FLOAT_LITERAL: '-'? NUMBER FloatSep NUMBER?;
STRING_LITERAL: '"' ~('\n' | '\r' | '"')* '"';
BOOL_LITERAL: 'true' | 'false';

Whitespace: [ \t\r\n\u000C]+ -> skip ;
Comment: BlockComment | LineComment;
Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;
