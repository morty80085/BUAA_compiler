package frontend.Ast.Exp;

import error.Error;
import error.ErrorRecorder;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import frontend.symbol.*;
import llvm_ir.Value;

import java.util.ArrayList;

public class LVal extends Node{
    public LVal(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    public boolean isConst() {
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        Symbol symbol = SymbolManager.getManager().getSymbolByName(name);
        if(symbol instanceof ConstSymbol) {
            return true;
        }
        return false;
    }

    @Override
    public Integer getDim() {
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        Symbol symbol = SymbolManager.getManager().getSymbolByName(name);
        if(symbol == null) {
            return null;
        }
        int dim = 0;
        int cnt = 0;
        if(symbol instanceof ConstSymbol && ((ConstSymbol) symbol).isArray()) {
            dim = 1;
        } else if(symbol instanceof VarSymbol && ((VarSymbol) symbol).isArray()) {
            dim = 1;
        } else if(symbol instanceof StaticSymbol && ((StaticSymbol) symbol).isArray()) {
            dim = 1;
        }
        for (Node child : children) {
            if (child instanceof TokenNode && ((TokenNode)child).getToken().getTokenType() == TokenType.LBRACK) {
                cnt++;
            }
        }
        return dim - cnt;
    }

    @Override
    public void visit() {
        //Error c
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        if(SymbolManager.getManager().getSymbolByName(name) == null) {
            ErrorRecorder.addError(new Error(Error.ErrorType.c, children.get(0).getEndLine()));
        }
        super.visit();
    }

    //用于生成在等号左边的LVal的语句
    public Value genIRForValue() {
        return null;
    }

    //用于生成在等号右边的LVal的语句(即生成一个地址)
    public Value genIRForAssign() {
        return null;
    }
}
