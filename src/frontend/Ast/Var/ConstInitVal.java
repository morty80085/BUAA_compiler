package frontend.Ast.Var;

import frontend.Ast.Exp.ConstExp;
import frontend.Node;
import frontend.SyntaxVarType;

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
            for(Node node : children) {
                if(node instanceof ConstInitVal) {
                    ArrayList<Integer> temp = ((ConstInitVal)node).execute(dim - 1);
                    ans.addAll(temp);
                }
            }
        }
        return ans;
    }
}
