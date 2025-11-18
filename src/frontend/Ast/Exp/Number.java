package frontend.Ast.Exp;

import frontend.Node;
import frontend.TokenNode;
import frontend.SyntaxVarType;
import llvm_ir.Constant;
import llvm_ir.Value;

import java.util.ArrayList;

public class Number extends Node{
    public Number(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Integer getDim() {
        return 0;
    }

    @Override
    public int execute() {
        String name = ((TokenNode)children.get(0)).getToken().getTokenContent();
        return Integer.parseInt(name);
    }

    @Override
    public Value genIR() {
        String name = ((TokenNode)children.get(0)).getToken().getTokenContent();
        return new Constant(Integer.parseInt(name));
    }
}
