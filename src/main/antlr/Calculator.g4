grammar Calculator;
INT    : [0-9]+;
DOUBLE : [0-9]+'.'[0-9]+;
PI     : 'pi';
E      : 'e';
POW    : '^';
NL     : '\n';
WS     : [ \t\r]+ -> skip;

PLUS  : '+';
EQUAL : '=';
MINUS : '-';
MULT  : '*';
DIV   : '/';
LPAR  : '(';
RPAR  : ')';

COS : 'cos';
SIN : 'sin';
TAN : 'tan';
ACOS : 'acos';
ASIN : 'asin';
ATAN : 'atan';
LN : 'ln';
LOG : 'log';
SQRT : 'sqrt';

ID     : [a-zA-Z_][a-zA-Z_0-9]*;

start
    : plusOrMinus
    ;

plusOrMinus
    : plusOrMinus PLUS multOrDiv  # Plus
    | plusOrMinus MINUS multOrDiv # Minus
    | multOrDiv                   # ToMultOrDiv
    ;

multOrDiv
    : multOrDiv MULT pow # Multiplication
    | multOrDiv DIV pow  # Division
    | pow                # ToPow
    ;

pow
    : unaryMinus (POW pow)? # Power
    ;

unaryMinus
    : MINUS unaryMinus # ChangeSign
    | func             # ToFunc
    | atom             # ToAtom
    ;

func
    : COS LPAR plusOrMinus RPAR # FuncCos
    | SIN LPAR plusOrMinus RPAR # FuncSin
    | TAN LPAR plusOrMinus RPAR # FuncTan
    | ATAN LPAR plusOrMinus RPAR # FuncAtan
    | ASIN LPAR plusOrMinus RPAR # FuncAsin
    | ACOS LPAR plusOrMinus RPAR # FuncAcos
    | LN LPAR plusOrMinus RPAR # FuncLn
    | LOG LPAR plusOrMinus RPAR # FuncLog
    | SQRT LPAR plusOrMinus RPAR # FuncSqrt
    ;

atom
    : PI                    # ConstantPI
    | E                     # ConstantE
    | DOUBLE                # Double
    | INT                   # Int
    | ID                    # Variable
    | LPAR plusOrMinus RPAR # Braces
    ;