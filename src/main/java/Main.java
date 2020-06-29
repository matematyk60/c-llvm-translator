import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import org.antlr.v4.runtime.tree.*;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class Main {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("input.txt");
        String input = readFile(inputFile);

        SimpleCLexer lexer = new SimpleCLexer(new ANTLRInputStream(input));
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        SimpleCParser parser = new SimpleCParser(commonTokenStream);

        ParseTree tree = parser.start();
        System.out.println(tree.toStringTree());

        LlvmTranslatorVisitor llvmTranslatorVisitor = new LlvmTranslatorVisitor();
        String g = llvmTranslatorVisitor.visit(tree);
        System.out.println(g);
    }

    private static String readFile(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
