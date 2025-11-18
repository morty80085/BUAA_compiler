package frontend.Ast.Func;

import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class FuncType extends Node{
    public FuncType(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public ValueType getFuncType() {
        TokenType tokenType = ((TokenNode) children.get(0)).getToken().getTokenType();
        if(tokenType == TokenType.INTTK) {
            return ValueType.INT;
        } else if(tokenType == TokenType.VOIDTK) {
            return ValueType.VOID;
        }
        return null;
    }
}
