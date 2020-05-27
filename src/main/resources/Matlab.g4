grammar Matlab;

//regula dla poczatku programu

poczatek : (blokFunkcyjny)? cialoProgramu;

blokFunkcyjny : (function)+;
cialoProgramu : stat;

//regula dla wyrazenia funcyjnego
//function: 'function' id operatorPrzyp id   leftParenthesis ( id (comma id )*)? rightParenthesis stat  'end';
function: 'function' id operatorPrzyp id leftParenthesis arguments rightParenthesis  stat  'end';

arguments: (id (comma id)*)?;

//regula dla wyrazenie
stat :  (  ifin | switchin | whilein | forin | przypisanie | element )  (stat)?;

//newLineComb : (N)?;

//regula dla instrukcji if
ifin : ifreg  (elseifereg)* (elsereg)? 'end';

ifreg : 'if' wyrazenieWarunkoweIn  stat;

elseifereg : 'elseif' wyrazenieWarunkoweIn   stat;
elsereg : 'else'  stat;

//regula dla instrukcji switch
switchin : 'switch' idReg (casereg)+ otherwisereg 'end';
 casereg : 'case' caseArgReg  stat;
 otherwisereg : 'otherwise'  stat;
 caseArgReg : liczbaCalkowita;
 idReg : id;
//regula dla instrukcji while

whilein : 'while' wyrazenieWarunkoweIn wnetrzePetli 'end';


//regula dla instrukcji for
forin: 'for' forReg wnetrzePetli 'end';
forReg : id operatorPrzyp forReg2;
forReg2 : liczba  colon liczba (colon liczba )? ;


wyrazenieWarunkoweIn : wyrazenieWarunkowe;
//regula dla wyraznia warunkowego
wyrazenieWarunkowe : not wyrazenieWarunkowe
                   | wyrazenieWarunkowe and wyrazenieWarunkowe
                   | wyrazenieWarunkowe or wyrazenieWarunkowe
                   | wyrazenieZwracajaceCos operatorPorownania wyrazenieZwracajaceCos //nie zmiescilo sie w linijce
                   |leftParenthesis wyrazenieWarunkowe rightParenthesis
                   ;

//regula dla wnetrza petli
wnetrzePetli : breakCom (wnetrzePetli)?
             | continueCom (wnetrzePetli)?
             | stat (wnetrzePetli)?
             ;

//regula dla przypisania wartosci do zmiennej
przypisanie : id operatorPrzyp wyrazenieZwracajaceCos semicolon;

//regula dla wyrazenia zwracajacego jakas wartosc
wyrazenieZwracajaceCos: wyrazenieZwracajaceCos operatorT
                      | wyrazenieZwracajaceCos (operatorP) wyrazenieZwracajaceCos
                      | wyrazenieZwracajaceCos (operatorM) wyrazenieZwracajaceCos
                      | wyrazenieZwracajaceCos (operatorA) wyrazenieZwracajaceCos
                      | funkcjaZwracajacaCos
                      | macierz
                      | liczbaUlamkowa
                      | liczbaCalkowita
                      | id
                      | leftParenthesis wyrazenieZwracajaceCos rightParenthesis
                      ;

//| podMacierz

//regula dla wywolania funkcji w ciele programu
funkcjaZwracajacaCos : id leftParenthesis (argumentyWywolania)? rightParenthesis;

//regula dla argumentow wywolanai funkcji
 argumentyWywolania:  wyrazenieZwracajaceCos (comma argumentyWywolania )?;


//regula dla operatorow porownania
operatorPorownania : '=='
                   | '~='
                   | '>='
                   | '>'
                   | '<='
                   | '<'
                   ;


//regula dla operatorow addytywnych
operatorA : '+'
          | '-'
          ;
//regula dla operatorow multiplikatywnych
operatorM : '*'
          | '/'
          | '.*'
          | './'
          ;

operatorT : '\'';

//regula dla operatorow potegowych + transpozycja
operatorP : '^'
          | '.^'
          ;

//regula dla operatora przypisania
operatorPrzyp : '=';

//regula dla elementu
element : (liczba | macierz| id) semicolon ;
//element : wyrazenieZwracajaceCos;
//podMacierz

//regula dla macierzy
liczba : liczbaUlamkowa
       | liczbaCalkowita
	   ;

//regula dla wybranych elementow podmacierzy
//podMacierz : id  leftParenthesis zakres rightParenthesis;

//reguly dla jakiegos zakresu z macierzy
//zakres : poczatekFragmentuZakresu(comma poczatekFragmentuZakresu)?;

//poczatekFragmentuZakresu : liczba (colon cdZakresu)?;

//cdZakresu:liczba(colon liczba)?;

//reguly dla macierzy
macierz: leftBracket liczba ((comma|semicolon) liczba)*  rightBracket;




//regula dla liczby ulamkowej
liczbaUlamkowa: Number+ '.' Number*;


//regula dla liczby calkowitej
liczbaCalkowita: Number+;

//regula dla identyfikatora
id : Letter (Letter | Number )*;

breakCom : 'break' semicolon;
continueCom : 'continue' semicolon;

//regula dla przecinka
comma : ',';

leftParenthesis : '(';
rightParenthesis : ')';
leftBracket : '[';
rightBracket : ']';
colon : ':';
semicolon : ';';
not : '~';
and : '&&';
or : '||';

Number : [0-9];
Letter : 'a'..'z' | 'A'..'Z' | '_';

//N:[\n]+;
WS:[ \t\r\n]+ -> skip ;