package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Node;
import frontend.Ast.Exp.Exp;
import frontend.SyntaxVarType;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolManager;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class ReturnStmt extends Node{
    public ReturnStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public void visit() {
        if(children.size() >= 2 && children.get(1) instanceof Exp) {
            FuncSymbol funcSymbol = SymbolManager.getManager().getLatestFunc();
            if(funcSymbol.getReturnType() == ValueType.VOID) {
                //Error f
                ErrorRecorder.addError(new Error(Error.ErrorType.f,children.get(0).getStartLine()));
            }
        }
        super.visit();
    }
}
