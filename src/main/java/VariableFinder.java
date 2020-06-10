import java.util.HashSet;
import java.util.Set;

public class VariableFinder extends CalculatorBaseVisitor<Void> {

    private final Set<String> varSet = new HashSet<>();

    @Override
    public Void visitVariable(CalculatorParser.VariableContext ctx) {
        varSet.add(ctx.ID().getSymbol().getText());
        return null;
    }

    public void flush() {
        varSet.clear();
    }

    public Set<String> getVarSet() {
        return varSet;
    }
}