package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.Value;

import java.util.ArrayList;
//PrimaryExp → '(' Exp ')' | LVal | Number
public class PrimaryExp extends Node{
    public PrimaryExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Integer getDim() {
        for(Node child : children) {
            if(child.getDim() != null) {
                return child.getDim();
            }
        }
        return null;
    }

    @Override
    public int execute() {
        int ans = 0;
        if(children.get(0) instanceof Number) {
            ans = children.get(0).execute();
        } else if(children.get(0) instanceof LVal) {
            ans =  children.get(0).execute();
        } else {
            ans = children.get(1).execute();
        }
        return ans;
    }

    @Override
    public Value genIR() {
        if(children.get(0) instanceof Number) {
            //Number
            return children.get(0).genIR();
        } else if (children.get(0) instanceof LVal) {
            //LVal
            return ((LVal) children.get(0)).genIRForValue();
        } else {
            //(Exp)
            return children.get(1).genIR();
        }
    }
}
