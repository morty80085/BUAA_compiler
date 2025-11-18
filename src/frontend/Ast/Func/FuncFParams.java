package frontend.Ast.Func;

import frontend.Node;
import frontend.SyntaxVarType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class FuncFParams extends Node{
    public FuncFParams(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public ArrayList<ValueType> getValueTypes() {
        ArrayList<ValueType> list = new ArrayList<>();
        for(Node child : children) {
            if(child instanceof  FuncFParam) {
                ValueType valueType = ((FuncFParam) child).getValueType();
                list.add(valueType);
            }
        }
        return list;
    }

    public ArrayList<Integer> getFuncFDimes() {
        ArrayList<Integer> list = new ArrayList<>();
        for(Node child : children) {
            if(child instanceof FuncFParam) {
                int dim = ((FuncFParam) child).getFuncFDim();
                list.add(dim);
            }
        }
        return list;
    }
}
