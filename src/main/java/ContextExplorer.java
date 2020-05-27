import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContextExplorer {
    public static void explore(RuleContext ruleContext, int indentationLevel) {
        String ruleName = CParser.ruleNames[ruleContext.getRuleIndex()];
        String indent = IntStream.range(0, indentationLevel).mapToObj(a -> " ").collect(Collectors.joining());
        System.out.println(indent + ruleName);
        if (ruleContext instanceof ParserRuleContext) {
                ((ParserRuleContext) ruleContext).children.forEach(child -> {
                if (child instanceof RuleContext) explore((RuleContext)child, indentationLevel + 2);
            });
        } ;

    }
}
