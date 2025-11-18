package frontend.Ast.Var;

import frontend.Ast.Exp.ConstExp;
import frontend.Ast.Exp.Exp;
import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.Value;

import java.util.ArrayList;

public class InitVal extends Node{
    public InitVal(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public ArrayList<Integer> execute(int dim) {
        ArrayList<Integer> ans = new ArrayList<>();
        if(dim == 0) {
            Exp exp = (Exp) children.get(0);
            ans.add(exp.execute());
        } else {
            for(Node node : children) {
                if(node instanceof InitVal) {
                    ArrayList<Integer> temp = ((InitVal)node).execute(dim - 1);
                    ans.addAll(temp);
                }
            }
        }
        return ans;
    }

    public ArrayList<Value> genIRList(int dim) {
        ArrayList<Value> ans = new ArrayList<>();
        if(dim == 0) {
            Value value = children.get(0).genIR();
            ans.add(value);
        } else {
            for(int i = 0; i < children.size(); i++) {
                if(children.get(i) instanceof Exp) {
                    Value value = children.get(i).genIR();
                    ans.add(value);
                }
            }
        }
        return ans;
    }
}
