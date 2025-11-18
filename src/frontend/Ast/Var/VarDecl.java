package frontend.Ast.Var;

import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.symbol.SymbolManager;

import java.util.ArrayList;

public class VarDecl extends Node{
    public VarDecl(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public void visit() {
        if(((TokenNode)children.get(0)).getToken().getTokenContent().equals("static")) {
            SymbolManager.getManager().setStatic(true);
        }
        super.visit();
        SymbolManager.getManager().setStatic(false);
    }

}
