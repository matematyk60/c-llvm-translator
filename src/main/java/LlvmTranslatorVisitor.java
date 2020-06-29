import java.io.FileWriter;
import java.io.IOException;


public class LlvmTranslatorVisitor extends SimpleCBaseVisitor<String> {
    FileWriter outputCode;
    int r_index;
    String tabs;

    @Override
    public String visitStart(SimpleCParser.StartContext ctx) {
        r_index = 1;
        tabs = "";
        return super.visitStart(ctx);
    }

    @Override
    public String visitEof(SimpleCParser.EofContext ctx) {
        if(outputCode!=null) {
            try {
                outputCode.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.visitEof(ctx);
    }

    @Override
    public String visitStatement(SimpleCParser.StatementContext ctx) {
        if(outputCode==null) {
            try {
                outputCode = new FileWriter("OutputCode.llvm", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.visitStatement(ctx);
    }

    @Override
    public String visitFor_statement(SimpleCParser.For_statementContext ctx) {
        try {
            outputCode.write(ctx.FOR().getText() + '(');
            visitFor_conditions(ctx.for_conditions());
            outputCode.write(") {\n");

            visitIf_or_loop_body(ctx.if_or_loop_body());

            outputCode.write("}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitFor_conditions(SimpleCParser.For_conditionsContext ctx) {
        try {
            outputCode.write("int " + ctx.IDENTIFIER(0) + '=' + ctx.INTEGER_LITERAL(0)+"; "+ctx.IDENTIFIER(1));
            visitComparison(ctx.comparison());
            outputCode.write(ctx.INTEGER_LITERAL(1)+"; "+ctx.IDENTIFIER(2));
            if(ctx.INC() != null){
                outputCode.write(String.valueOf(ctx.INC()) + ';');
            }
            else{
                outputCode.write(String.valueOf(ctx.DEC()) + ';');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.visitFor_conditions(ctx);
    }

    @Override
    public String visitWhile_statement(SimpleCParser.While_statementContext ctx) {
        try {
            outputCode.write(ctx.WHILE().getText() + '(');
            visitBoolean_expression(ctx.boolean_expression());
            outputCode.write(") {\n");
            visitIf_or_loop_body(ctx.if_or_loop_body());
            outputCode.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitBoolean_expression(SimpleCParser.Boolean_expressionContext ctx) {
        return super.visitBoolean_expression(ctx);

    }

    @Override
    public String visitComparison(SimpleCParser.ComparisonContext ctx) {
        try {
            if(ctx.NOT_EQUALS() != null) {
                outputCode.write("!=");
            } else if(ctx.EQUALS() != null) {
                outputCode.write("==");
            } else {
                outputCode.write(ctx.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitExpression(SimpleCParser.ExpressionContext ctx) {
        visitPrimary_expression(ctx.primary_expression(0));
        visitMath_operator(ctx.math_operator());
        visitPrimary_expression(ctx.primary_expression(1));
        return null;
    }

    @Override
    public String visitPrimary_expression(SimpleCParser.Primary_expressionContext ctx) {
        try {
            outputCode.write(ctx.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitMath_operator(SimpleCParser.Math_operatorContext ctx) {
        try {
            outputCode.write(ctx.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitPrint(SimpleCParser.PrintContext ctx) {
        try {
            outputCode.write("System.out.println" + ctx.LEFT_ROUND_BRACKET());
            visitString(ctx.string());
            outputCode.write(ctx.RIGHT_ROUND_BRACKET().getText() + ';' + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitString(SimpleCParser.StringContext ctx) {
        try {
            outputCode.write(ctx.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitFunction_argument(SimpleCParser.Function_argumentContext ctx) {
        try{
            if(ctx.table()==null)
                outputCode.write("i32 %arg_" + ctx.IDENTIFIER().getText());
            else
                outputCode.write("i32* %arg_" + ctx.IDENTIFIER().getText());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String visitFunction_statement(SimpleCParser.Function_statementContext ctx) {
        try{
            outputCode.write("define ");
            if(ctx.type().getText().equals("void"))
                outputCode.write("void @");
            else outputCode.write("i32 @");
            outputCode.write(ctx.IDENTIFIER().getText() + "(");
            for(int index = 0; index<ctx.function_argument().size(); index++){
                visitFunction_argument(ctx.function_argument(index));
                if(index != ctx.function_argument().size() - 1)
                    outputCode.write(", ");
            }

            outputCode.write(") {\n");
            tabs += "\t";
            for(int index = 0; index<ctx.function_argument().size(); index++){
//                store i32 %a_arg, i32* %a
//                store i32* %b_arg, i32** %b
                outputCode.write(tabs + "%" + ctx.function_argument(index).IDENTIFIER().getText() + "= alloca i32");
                if(ctx.function_argument(index).table()!=null){
                    outputCode.write("*\n" + tabs + "store i32* %arg_" + ctx.function_argument(index).IDENTIFIER().getText() + " i32** %" +ctx.function_argument(index).IDENTIFIER().getText() + "\n");
                }
                else{
                    outputCode.write("\n" + tabs + "store i32 %arg_" + ctx.function_argument(index).IDENTIFIER().getText() + " i32* %" +ctx.function_argument(index).IDENTIFIER().getText() + "\n");

                }


            }
            for(SimpleCParser.StatementContext statement: ctx.statement()){
                visitStatement(statement);
            }
            if(ctx.type().getText().equals("void"))
                outputCode.write(tabs + "ret void\n");
            else{
                outputCode.write(tabs + "ret i32");
                visitReturn_statement(ctx.return_statement());
            }

            outputCode.write("} \n");
            tabs = tabs.substring(0, tabs.length()-1);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitReturn_statement(SimpleCParser.Return_statementContext ctx) {
        try{
            outputCode.write(" " + ctx.IDENTIFIER().getText() + "\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return super.visitReturn_statement(ctx);
    }

    @Override
    public String visitVariable_declaration(SimpleCParser.Variable_declarationContext ctx) {
        try {
            if(ctx.IDENTIFIER() != null) {
                outputCode.write(tabs + "%" + ctx.IDENTIFIER().getText());
                if(ctx.table() != null){
                    outputCode.write("=[" + ctx.table().INTEGER_LITERAL() + " x i32] zeroinitializer\n");
                }
                else if(ctx.type().getText().equals("int"))
                    outputCode.write( "=alloca i32\n");
                return null;
            }

            if(ctx.type() == null) {
                visitAssignment(ctx.assignment());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.visitVariable_declaration(ctx);
    }

    @Override
    public String visitAssignment(SimpleCParser.AssignmentContext ctx) {
        try {
            if(ctx.literal() != null) {
                outputCode.write(tabs + "%" + ctx.IDENTIFIER().getText() + ctx.ASSIGN() +"alloca i32\n");
                outputCode.write(tabs + "%r" + r_index + ctx.ASSIGN() + "add i32 " + ctx.literal().INTEGER_LITERAL() + ", 0\n");
                outputCode.write(tabs + "store i32 " + " %r" + r_index + ", i32* %" + ctx.IDENTIFIER().getText()+"\n");
            } else if(ctx.expression() != null) {
                outputCode.write(ctx.IDENTIFIER().getText() + " " + ctx.ASSIGN() + " " + ctx.expression().getText() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        r_index++;
        return null;
    }

    @Override
    public String visitIf_statement(SimpleCParser.If_statementContext ctx) {
        try {
            outputCode.write(tabs + "%r"+r_index + "=");
            r_index++;
            String var = ((SimpleCParser.Boolean_expressionContext) ctx.children.get(2)).primary_expression(0).getText();
            String var2 = ((SimpleCParser.Boolean_expressionContext) ctx.children.get(2)).primary_expression(1).getText();
            String comp1 = ((SimpleCParser.Boolean_expressionContext) ctx.children.get(2)).comparison().getText();
            String comp = "";
            switch (comp1){
                case "==":
                    comp = "eq";
                    break;
                case "<":
                    comp ="slt";
                    break;
                case ">":
                    comp ="sgt";
                    break;
                case ">=":
                    comp ="sge";
                    break;
                case "<=":
                    comp = "sgl";
                    break;
            }
            try {
                int tmp = Integer.parseInt(var);
                outputCode.write("add i32 "+ tmp +", 0\n");

            } catch(NumberFormatException e) {
                outputCode.write("load i32* %" + var + "\n");
            }
            outputCode.write(tabs + "%r" + r_index + "=");
            r_index++;
            try {
                int tmp = Integer.parseInt(var2);
                outputCode.write("add i32 "+ tmp +", 0\n");

            } catch(NumberFormatException e) {
                outputCode.write("load i32* %" + var2 + "\n");
            }
            outputCode.write(tabs + "r"+ r_index + "=" + "icmp " + comp + " i32 %r" + (r_index - 2) + " %r" + (r_index -1) + "\n");
            outputCode.write(tabs + "br i1 %r" + r_index +", label %true"+r_index+ ", label %false"+r_index+"\n");
            r_index++;
                if(ctx.trueStatement != null) {
                    outputCode.write(tabs + "true"+(r_index-1)+":\n");
                    tabs += "\t";
                    visitIf_or_loop_body(ctx.if_or_loop_body(0));
                    tabs = tabs.substring(0, tabs.length()-1);
            }
            if(ctx.ELSE() != null) {
                outputCode.write(tabs + "false"+(r_index-1)+"\n");
                tabs += "\t";
                outputCode.write(" else {\n");
                if(ctx.falseStatement != null) {
                    visitIf_or_loop_body(ctx.if_or_loop_body(1));
                }
                outputCode.write(tabs + "true"+r_index+":\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String visitIf_or_loop_body(SimpleCParser.If_or_loop_bodyContext ctx) {
        return super.visitIf_or_loop_body(ctx);
    }
}
