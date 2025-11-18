package frontend.Ast.Var;

import frontend.Ast.Exp.ConstExp;
import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.Value;

import java.util.ArrayList;

public class ConstInitVal extends Node {
    public ConstInitVal(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    public ArrayList<Integer> execute(int dim) {
        ArrayList<Integer> ans = new ArrayList<>();
        if(dim == 0) {
            ConstExp constExp = (ConstExp) children.get(0);
            ans.add(constExp.execute());
        } else {
            for(int i = 0; i < children.size(); i++) {
                if(children.get(i) instanceof ConstExp) {
                    ConstExp constExp = (ConstExp) children.get(i);
                    ans.add(constExp.execute());
                }
            }
        }
        return ans;
    }
}
