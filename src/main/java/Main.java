import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.*;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    //    public static void main(String[] args) {
//            String exampleCode = "int i = 5;";
//            CLexer lexer = new CLexer(new ANTLRInputStream(exampleCode));
//            TokenStream commonTokenStream = new CommonTokenStream(lexer);
//            SimpleCParser parser = new SimpleCParser(commonTokenStream);
//
//            ParseTree tree = parser.start();
//            System.out.println(tree.toStringTree());
//            System.out.println("BREAK");
//
//            AVisitor visitor = new AVisitor();
//            System.out.println(visitor.visit(tree));
//
//
//    }
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
