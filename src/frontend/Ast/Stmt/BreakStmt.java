package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.symbol.SymbolManager;

import java.util.ArrayList;

public class BreakStmt extends Node{
    public BreakStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public void visit() {
        //Error m
        if(SymbolManager.getManager().getLoopDepth() <= 0) {
            ErrorRecorder.addError(new error.Error(Error.ErrorType.m, endLine));
        }
        super.visit();
    }
}
