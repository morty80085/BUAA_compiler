package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Exp.Exp;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.StringLiteral;
import llvm_ir.Value;
import llvm_ir.instr.IOInstr;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Value genIR() {
        List<Value> expValueList = new ArrayList<>();
        String str = formatString.substring(1, formatString.length() - 1);
        StringBuilder sb = new StringBuilder();
        for (Exp child : expList) {
            Value irValue = child.genIR();
            expValueList.add(irValue);
        }
        int expCnt = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '%') {
                if(sb.length() != 0) {
                    StringLiteral stringLiteral = new StringLiteral(IRBuilder.getInstance().genStringLiteralName(), sb.toString());
                    new IOInstr.PutStr(IRBuilder.getInstance().genVarName(), stringLiteral);
                    sb.setLength(0);
                }
                Value value = expValueList.get(expCnt);
                Instr instr = new IOInstr.PutInt(IRBuilder.getInstance().genVarName(), value);
                expCnt = expCnt + 1;
                i = i + 1;
            } else if(str.charAt(i) == '\\') {
                sb.append("\n");
                i = i + 1;
            } else {
                sb.append(str.charAt(i));
            }
        }
        if(sb.length() != 0) {
            StringLiteral stringLiteral = new StringLiteral(IRBuilder.getInstance().genStringLiteralName(), sb.toString());
            new IOInstr.PutStr(IRBuilder.getInstance().genVarName(), stringLiteral);
        }
        return null;
    }
}
