import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {

        String path = Paths.get(Main.class.getClassLoader().getResource("example.c").toURI()).toString();
        ANTLRFileStream antlrFileStream = new ANTLRFileStream(path);
        CLexer lexer = new CLexer(antlrFileStream);

        TokenStream tokenStream = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokenStream);

        CParser.CompilationUnitContext compilationUnit = parser.compilationUnit();

//        AVisitor visitor = new AVisitor();

        ContextExplorer.explore(compilationUnit, 0);

    }
}
