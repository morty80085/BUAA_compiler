package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Exp.LVal;
import frontend.Node;
import frontend.SyntaxVarType;

import java.util.ArrayList;

public class ForStmt extends Node{
    public ArrayList<LVal> lValArrayList = new ArrayList<>();

    public ForStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
        for(Node child : children) {
            if(child instanceof LVal) {
                lValArrayList.add((LVal) child);
            }
        }
    }

    @Override
    public void visit() {
        for(LVal child : lValArrayList) {
            if(child.isConst()) {
                ErrorRecorder.addError(new Error(Error.ErrorType.h,startLine));
            }
        }
        super.visit();
    }
}
