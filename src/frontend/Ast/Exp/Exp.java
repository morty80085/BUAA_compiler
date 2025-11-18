package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.Value;

import java.util.ArrayList;

public class Exp extends Node{
    public Exp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Integer getDim() {
        for(Node child : children) {
            if(child.getDim() != null) {
                return  child.getDim();
            }
        }
        return null;
    }

    @Override
    public int execute() {
        return children.get(0).execute();
    }

    @Override
    public Value genIR() {
        return children.get(0).genIR();
    }
}
