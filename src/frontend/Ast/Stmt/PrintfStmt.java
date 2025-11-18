package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Exp.Exp;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;

import java.util.ArrayList;

public class PrintfStmt extends Node{
    private String formatString;
    private ArrayList<Exp> expList;

    public PrintfStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
        this.expList = new ArrayList<>();
        for(Node child : children) {
            //formatString
            if(child instanceof TokenNode && ((TokenNode) child).getToken().getTokenType() == TokenType.STRCON) {
                formatString = ((TokenNode) child).getToken().getTokenContent();
            }
            if(child instanceof Exp) {
                expList.add((Exp) child);
            }
        }
    }

    @Override
    public void visit() {
        //统计%d个数
        int cnt = 0;
        //去除""
        for(int i = 1; i < formatString.length() - 1; i++) {
            if(formatString.charAt(i) == '%' && formatString.charAt(i + 1) == 'd') {
                cnt++;
            }
        }
        //Error l
        if(cnt != expList.size()) {
            ErrorRecorder.addError(new Error(Error.ErrorType.l, startLine));
        }
        super.visit();
    }
}
