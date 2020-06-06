import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.*;

public class Main {

    public static void main(String[] args) {
            String exampleCode = "int i = 5;";
            CLexer lexer = new CLexer(new ANTLRInputStream(exampleCode));
            TokenStream commonTokenStream = new CommonTokenStream(lexer);
            SimpleCParser parser = new SimpleCParser(commonTokenStream);

            ParseTree tree = parser.start();
            System.out.println(tree.toStringTree());
            System.out.println("BREAK");

            AVisitor visitor = new AVisitor();
            System.out.println(visitor.visit(tree));


    }
}
