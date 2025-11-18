package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Exp.LVal;
import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class AssignStmt extends Node{
    public AssignStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public void visit() {
        LVal lVal = (LVal) children.get(0);
        if(lVal.isConst()) {
            ErrorRecorder.addError(new Error(Error.ErrorType.h,startLine));
        }
        super.visit();
    }
}
