# kompilatory

Autorzy: Aleksandra Kasznia, Dawid Godek

# Dostępne technologie do tworzenia translatorów

Translator można napisać praktycznie w każdym języku programowania, można jednak zauważyć, że najczęściej w tym celu wykorzystywane są język C, C++, C#, java, python.

Języki C oraz C++ są językami niższego poziomu co zazwyczaj skutkuje bardziej wydajnym programem. Rozwiązania napisane w tych językach zazwyczaj nie korzystają ze specjalnych bibliotek (przeznaczonych głównie do tworzenia translatorów, parserów czy kompilatorów), a programy są bardzo długie. Aby wybór tej technologii nie obrócił się przeciwko programiście, powinien on być dość dobrze zaznajomiony z tymi językami.

C# oraz python oferuje mnogość gotowych rozwiązań. Są biblioteki, które same nie tylko mogą tłumaczyć program, ale również kompilować go do innego języka. W pythonie najbardziej znana jest chyba cython – biblioteka dzięki której możemy skompilować nasz program pythonowy do C. W C# praktycznie nie udało nam się znaleźć biblioteki, która mogłabymspełniać nasze wymagania oraz nie być dodatkowo płatną. Natomiast python posiada naprawdę dużo takich rozwiązań, jednak w większości są to rozwiązania stworzone przez internautów i niekoniecznie wydajne. Dodatkowo python jest językiem bardzo wysokiego poziomu co znacząco zmniejsza kontrolę, którą mamy nad tworzonym programem.

Najbardziej rozponawalnym w środowisku generatorem parserów jest prawdopodobnie YACC (Yet Another Compiler Compiler), który jest jednym z narzędzi deweloperskich Unix’a, ma już ponad 45 lat (piewszy paper tyczący się YACC ukazał się w roku 1975 [https://www.cs.utexas.edu/users/novak/yaccpaper.htm](https://www.cs.utexas.edu/users/novak/yaccpaper.htm)). Generuje on parsery w języku C, w którym nie mamy biegłości. Nie chcemy także korzystać z historycznego narzędzia. Warto wspomnieć, że na podstawie projektu YACC powstało wiele innych generatorów parserów: CUP, Bison, btyacc, YACC++; lecz nie decydujemy się na ich użycie, ponieważ część z nich posiada komercyjne licencje oraz nie są to narzędzia będące dzisiejszym standardem w branży.

Warto wspomnieć również o takich programach jak Bison- generator parserów utworzony w ramach projektu GNU czy SableCC – generator parserów typu LALR dla języka Java, jednak nie spełniają one całkowicie naszych wymagań.

Stwierdziliśmy, że idealnym rozwiązaniem do naszych potrzeb jest ANTLR – generator parserów generujący (obecnie) parsery w językach Java, python, C++, C, C#, JavaScript, Go, Swift, PHP. Generuje on parsery typu LL(*) i wspiera analizę składniową. Dodatkowo posiada wbudowane już gramatyki dla niektórych języków programowania, jednocześnie umożliwiając nam wprowadzenie własnej (w formacie podobnym do EBNF). Zalety ANTLR, na które szczególnie zwróciliśmy uwagę:

- jest narzędziem darmowym i opensourceowym.
- jest obecnie standardem w branży opensourceowych generatorów parserów; liczba gwiazdek na githubie: 7700, liczba kontrybutorów: 194 (dla porównania SableCC odpowiednio 115, 8)
- generuje drzewiaste parsery oparte o intuicyjny wzorzec projektowy **Visitor** i rekurencję
- charakteryzuje się dobrym raportowaniem błedów przez co debugowanie i praca z kodem jest o wiele łatwiejsza
- generuje parsery w języku Java, z którym posiadamy doświadczenie
- gramatyki dla wielu języków są już zdefiniowane

ANTLRem możemy wygenerować dla naszej gramatyki lekser i parser w oczekiwanym języku, następnie otrzymamy reprezentację za pomocą drzewa składniowego (AST). Ważna uwaga o której musimy pamiętać, aby móc skorzystać z ANTLR – wprowadzona przez nas gramatyka nie może być lewostronnie rekurencyjna.

[https://lh6.googleusercontent.com/SkGJW9V4b91cac9_m8esC0uB_1tO_OozxFJIfPXZ5VkLhx17zw5B9CKfImAhVqlyPWvNb41wIAROoZKZu0Q8N62bLp23clLsDLpixwyvVyyAAPCOwaqoUaNEeBZ1TVhOpVHbbTyO](https://lh6.googleusercontent.com/SkGJW9V4b91cac9_m8esC0uB_1tO_OozxFJIfPXZ5VkLhx17zw5B9CKfImAhVqlyPWvNb41wIAROoZKZu0Q8N62bLp23clLsDLpixwyvVyyAAPCOwaqoUaNEeBZ1TVhOpVHbbTyO)

Porównanie dostępnych narzędzi

# Przykład działania ANTLR4

Dla kogoś kto nie miał do czynienia z generatorami parserów ich działanie może być mało zrozumiałe i mało intuicyjne. Dlatego przedstawimy tutaj dość nietypowe zastosowanie ANTLR. Stworzymy gramatykę dowolnej funkcji matematycznej. Następnie wykorzystamy ANTLR do dwóch rzeczy:

- do znalezienia wszystkich zmiennych w danej funkcji (arność funkcji)
- do obliczenia wartości funkcji dla zadanych wartość zmiennych

## Gramatyka

Zdefiniujmy prostą gramatykę dla funkcji matematycznej:

```
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
```

Napisana gramatyka definiuje nam możliwe wyrażenia w gramatyce, stałe oraz priorytety wyrażeń. Podana gramatyka jest mało skomplikowana, co pozwala na łatwe zrozumienie struktury pliku gramatyki ANTLR4 `.g4`.

## Kod ANTLR4

Na podstawie gramatyki ANTLR generuje lekser oraz parser. Najbardziej przydatne dla nas jednak są klasy pozwalające przeprocesować wygenerowane drzewo parsowania. Mowa o klasach `Visitor` oraz `Listener`. Przyjrzyjmy się części klasy CalculatorVisitor wygenerowanej  przez ANTLR dla gramatyki kalkulatora. 

```java
public interface CalculatorVisitor<T> extends ParseTreeVisitor<T> {
		...
    T visitToMultOrDiv(CalculatorParser.ToMultOrDivContext ctx);
    T visitPlus(CalculatorParser.PlusContext ctx);
		T visitMinus(CalculatorParser.MinusContext ctx);
		...
}
```

Klasa jest parametryzowana typem `T`, który w implementacji interfejsu powinien reprezentować dane, które chcemy z danego drzewa parsowania wyciągnąć. Może to być wartość funkcji, a może to być tekst reprezentujący ten sam program w innym języku.

Dla każdego wyrażenia wygenerowana jest metoda, która je reprezentuje. W argumencie `ctx` dostępne są składniki tego wyrażenia. Dla przykładu: patrząc na gramatykę, w `visitPlus` mamy dostępne  składniki: wyrażenie `plusOrMinus`, atom `PLUS` oraz wyrażenie `multOrDiv`.

Oby odnaleźć wszystkie zmienne w danym drzewie parsowania, powinniśmy zaimplementować metodę `T visitVariable(CalculatorParser.VariableContext ctx);`, która zostanie wywołana za każdym razem gdy visitor napotka zmienną (atom `ID` z gramatyki). 

Oto przykład klasy, która każdą napotkaną zmienną w drzewie zapisze w kolekcji:

```java
public class VariableFinder extends CalculatorBaseVisitor<Void> {

    private final Set<String> varSet = new HashSet<>();

    @Override
    public Void visitVariable(CalculatorParser.VariableContext ctx) {
        varSet.add(ctx.ID().getSymbol().getText());
        return null;
    }

    public Set<String> getVarSet() {
        return varSet;
    }
}
```

Visitor jest tutaj parametryzowany typem `Void`, gdyż nie chcemy tworzyć żadnej struktury z drzewa parsowania. Chcemy tylko podejrzeć ile i jakie zmienne zawierają się w zadanej funkcji matematycznej.
Aby policzyć wartość wyrażenie, stworzymy klasę, która dla napotkanych wyrażeń zamieni je na wartości liczbowe używając biblioteki `Math` z standardowej biblioteki Java. 

```java
class ValueCalculator extends CalculatorBaseVisitor<Double> {

    private final Map<String, Double> variableMap;

    ValueCalculator(Map<String, Double> variableMap) {
        this.variableMap = variableMap;
    }

    @Override
    public Double visitStart(CalculatorParser.StartContext ctx) {
        return visit(ctx.plusOrMinus());
    }

    public Double visitPlus(CalculatorParser.PlusContext ctx) {
        return visit(ctx.plusOrMinus()) + visit(ctx.multOrDiv());
    }

    public Double visitMinus(CalculatorParser.MinusContext ctx) {
        return visit(ctx.plusOrMinus()) - visit(ctx.multOrDiv());
    }

    public Double visitMultiplication(CalculatorParser.MultiplicationContext ctx) {
        return visit(ctx.multOrDiv()) * visit(ctx.pow());
    }

    public Double visitDivision(CalculatorParser.DivisionContext ctx) {
        return visit(ctx.multOrDiv()) / visit(ctx.pow());
    }

    public Double visitPower(CalculatorParser.PowerContext ctx) {
        if (ctx.pow() != null) return Math.pow(visit(ctx.unaryMinus()), visit(ctx.pow()));
        else return visit(ctx.unaryMinus());
    }

    public Double visitChangeSign(CalculatorParser.ChangeSignContext ctx) {
        return -1 * visit(ctx.unaryMinus());
    }

    public Double visitConstantPI(CalculatorParser.ConstantPIContext ctx) {
        return Math.PI;
    }

    public Double visitConstantE(CalculatorParser.ConstantEContext ctx) {
        return Math.E;
    }

    public Double visitDouble(CalculatorParser.DoubleContext ctx) {
        return Double.valueOf(ctx.DOUBLE().getText());
    }

    public Double visitInt(CalculatorParser.IntContext ctx) {
        return Double.valueOf(ctx.INT().getText());
    }

    public Double visitVariable(CalculatorParser.VariableContext ctx) {
        String varName = ctx.ID().getSymbol().getText();
        return variableMap.get(varName);
    }

    public Double visitBraces(CalculatorParser.BracesContext ctx) {
        return visit(ctx.plusOrMinus());
    }

    public Double visitFuncCos(CalculatorParser.FuncCosContext ctx) {
        return Math.cos(visit(ctx.plusOrMinus()));
    }

    public Double visitFuncSin(CalculatorParser.FuncSinContext ctx) {
        return Math.sin(visit(ctx.plusOrMinus()));
    }

    public Double visitFuncTan(CalculatorParser.FuncTanContext ctx) {
        return Math.tan(visit(ctx.plusOrMinus()));
    }

    public Double visitFuncAtan(CalculatorParser.FuncAtanContext ctx) {
        return Math.atan(visit(ctx.plusOrMinus()));
    }

    public Double visitFuncAsin(CalculatorParser.FuncAsinContext ctx) {
        return Math.asin(visit(ctx.plusOrMinus()));
    }

    public Double visitFuncAcos(CalculatorParser.FuncAcosContext ctx) {
        return Math.acos(visit(ctx.plusOrMinus()));
    }
xxxx5x5
    public Double visitFuncLn(CalculatorParser.FuncLnContext ctx) {
        return Math.log(visit(ctx.plusOrMinus()));
    }

    public Double visitFuncLog(CalculatorParser.FuncLogContext ctx) {
        return Math.log10(visit(ctx.plusOrMinus()));
    }

    public Double visitFuncSqrt(CalculatorParser.FuncSqrtContext ctx) {
        return Math.sqrt(visit(ctx.plusOrMinus()));
    }

}
```

Zwróćmy uwage na metodę `visitVariable`. Jeżeli w drzewie parsowania znaleźliśmy identyfikator zmiennej, musimy go jakoś zamienić na wartość liczbową. W tym celu klasa przyjmuje w konstruktorze Mapę `<String, Double>`, która zawiera w sobie wartości, jakie mają przyjąć dane zmienne, np: `{"x": 23.44, "y": -230}`.

Użycie tych dwóch klas razem pozwala na stworzenie prostego interaktywnego programu, pozwalającego na obliczenie wartości dowolnej funkcji matematycznej. Program:

1. pobiera od użytkownika literał funkcji
2. używa klasy VariableFinder do odnalezienia wszystkich zmiennych w funkcji
3. pobiera od użytkownika wartość dla każdej odnalezionej zmiennej 
4. wylicza wartość funkcji dla zadanych argumentów

```java
public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Write function literal...");
        String functionLiteral = in.nextLine();
        CalculatorLexer lexer = new CalculatorLexer(new ANTLRInputStream(functionLiteral));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokenStream);

        CalculatorParser.StartContext parseTree = parser.start();

        VariableFinder variableFinder = new VariableFinder();
        variableFinder.visit(parseTree);

        HashMap<String, Double> variableMap = new HashMap<>();

        variableFinder.getVarSet().forEach(variable -> {
            System.out.println("Value for [" + variable + "] variable?");
            Double value = Double.valueOf((in.nextLine()));
            variableMap.put(variable, value);
        });

        ValueCalculator valueCalculator = new ValueCalculator(variableMap);
        Double result = valueCalculator.visit(parseTree);

        System.out.println("Result is: " + result);
    }
}
```

## Przykład działania

```
Write function literal...
> (x^2 + sin(4 * y) - cos(5 * z)) ^ 2 + 5
Value for [x] variable?
> 17
Value for [y] variable?
> 20
Value for [z] variable?
> -15
Result is: 82422.42980044847
```

## Translator C na LLVM

Podobne podejście stosujemy w naszym translatorze. Na podstawie napisanej przez nas gramatyki języka C, używając ANTLR4, generujemy lexer, parser oraz visitor. Za ich pomocą budujemy drzewo dla wprowadzonego zdania w języku C, a następnie przechodzimy przez nie przy użyciu visitora.

## Visitor vs Listener

Visitor jest bardziej elastyczny. Metody Listenera są wołane przez obiekt przechodzący po drzewie, zapewniony przez ANTRL. Natomiast Visitor sam przechodzi przez drzewo. W efekcie przy Visitorze możemy decydować o kierunku, w którym przechodzimy po drzewie lub przechodzić przez konkretne węzły więcej niż raz. W Listenerze reagujemy jedynie na fakt odwiedzenia węzła przez gotowy mechanizm. Listener działa na zasadzie callbacków wywoływanych po odwiedzenia węzła. Oba wzorce korzystają z różnych mechanizmów zarządzania pamięcią. Jeśli nasz input może być potencjalnie bezgraniczny, wtedy należy zastosować Listener, jako że przy Visitorze mogą pojawić się problemy z zarządzaniem pamięcią. Visitor działa lepiej jeśli potrzebujemy wartości zwracanych przez aplikację, ponieważ możemy skorzystać z wbudowanego mechanizmu zwracania wartości Javy.

## Przykładowe elementy gramatyki języka C

## przypisanie wartości do zmiennej

```jsx
assignment : IDENTIFIER ASSIGN literal | IDENTIFIER ASSIGN expression;
```

Mamy tutaj dwie produkcje. Obydwie zaczynają się od dwóch symboli terminalnych `IDENTIFIER` i `ASSIGN`.  Tak zdefiniowane są symbole terminalne w gramatyce ANTLR4:

```jsx
IDENTIFIER : LETTER LETTER_OR_DIGIT*;
fragment LETTER : [a-zA-Z_];
fragment LETTER_OR_DIGIT : [a-zA-Z0-9_];

ASSIGN : '=';
```

Jedna produkcja pozwala przypisać wartość do zmiennej za pomocą literału wartości (np. `8`, `8.4`, `true`, `"VALUE"`), a druga pozwala używać operatorów do modyfikacji wartości (np. `89 % 10`). 

```jsx
literal : INTEGER_LITERAL | FLOATING_POINT_LITERAL | STRING_LITERAL | BOOL_LITERAL ;

INT_LITERAL: '-'? NUMBER;
FLOAT_LITERAL: '-'? NUMBER FloatSep NUMBER?;
STRING_LITERAL: '"' ~('\n' | '\r' | '"')* '"';
BOOL_LITERAL: 'true' | 'false';

INTEGER_LITERAL : MINUS? DECIMAL_NUMERAL ;
FLOATING_POINT_LITERAL : MINUS? DECIMAL_NUMERAL FLOATING_POINT_SEPARATOR DECIMAL_NUMERAL? ;
NUMBER_LITERAL :INTEGER_LITERAL | FLOATING_POINT_LITERAL ;
STRING_LITERAL : QUOTE ~('\n' | '\r' | '"')* QUOTE ;
BOOL_LITERAL :  'true' | 'false' ;

expression
    : primary_expression math_operator primary_expression ;
```

Tych produkcji używamy do generowania drzewa parsowania i przechodzimy po nim używając klasy `LlvmTranslatorVisitor`, która na podstawie wygenerowanych produkcji i tokenów zapisuje do osobnego pliku odpowiadający kod pośredni LLVM. Przykład:

kod c:

```c
c = 30;
```

kod LLVM IR:

```c
%c=alloca i32
%r1=add i32 30, 0
store i32 %r1, i32* %c
```

## if statement

```jsx
ifStatement: IF '(' booleanExpression ')' Newline? '{' Newline statement* '}' Newline (ELSE '{' statement* '}' )?;
```

Rdzeniem tej reguły naszej gramatyki jest symbol nieterminalny `booleanExpression`, który rozwija się do kilku sposobów na otrzymanie wartości `true` lub `false`.

```jsx
booleanExpression
: expression comparison expression
| expression comparison primaryExpression
| primaryExpression comparison primaryExpression
| booleanExpression logicOperator booleanExpression
| primaryExpression;
```

Po przejściu wygenerowanego drzewa dla danego if statement zostaje zapisany odpowiedni kod pośredni LLVM;

```jsx
%r1=load i32* %c
%r2=add i32 15, 0
r3=icmp sgl i32 %r1 %r2
br i1 %r3, label %true3, label %false3
true3:
	%c=alloca i32
	%r4=add i32 30, 0
	store i32  %r4, i32* %c
```

## definicja funkcji

```jsx
function_statement: type IDENTIFIER '(' (function_argument ','?)+ ')' '{' (statement+)? return_statement?'}' | type IDENTIFIER '('')' '{' (statement+)? return_statement? '}';
function_argument: type IDENTIFIER table?;
return_statement: 'return ' (variable_declaration | IDENTIFIER) ';';
```

kod C:

```c
int g(int a, int b[10]) {
    a = 10;
    return a;
}
```

wygenerowany kod pośredni LLVM:

```jsx
define i32 @g(i32 %arg_a, i32* %arg_b) {
	%a= alloca i32
	store i32 %arg_a i32* %a
	%b= alloca i32*
	store i32* %arg_b i32** %b
	%a=alloca i32
	%r1=add i32 10, 0
	store i32  %r1, i32* %a
	ret i32 a
}
```

# Podsumowanie

W podsumowaniu należy podkreślić trafność doboru narzędzia, jakim jest `ANTLR4`. W przystępny sposób i przy umiarkowanym nakładzie pracy umożliwia wykorzystanie wiedzy o językach formalnych i kompilatorach do stworzenia dowolnego translatora. 

Mimo tego, że projekt udało się zrealizować w ramach jednego semestru przedmiotu Teoria kompilacji i kompilatory w kilka miesięcy, to w naszym odczuciu stworzenie gramatyki, która bierze pod uwagę wszystkie elementy języka C nie jest możliwa do stworzenia w tak krótkim czasie. Ponadto, według wykonanej analizy literatury i internetu okazuje się, że gramatyka w pełni opisująca język C nie jest gramatyką klasy 2 w hierarchi Chomsky'iego (bezkontekstowa), tylko typu pierwszego (kontekstowa). Link do artykułu: [https://en.wikipedia.org/wiki/Lexer_hack](https://en.wikipedia.org/wiki/Lexer_hack)