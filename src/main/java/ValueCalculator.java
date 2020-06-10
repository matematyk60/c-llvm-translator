import java.util.Map;

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