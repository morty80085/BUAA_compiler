package frontend;

import frontend.symbol.*;
import llvm_ir.Value;

import java.util.ArrayList;

public class CompUnit extends Node{
    public CompUnit(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public FuncSymbol createGetInt() {
        return new FuncSymbol("getint", SymbolType.GetIntFunc,ValueType.INT,new ArrayList<>(),new ArrayList<>());
    }

    @Override
    public void visit() {
        SymbolManager.getManager().setGlobal(true);
        SymbolManager.getManager().enterBlock();
        Symbol getInt = createGetInt();
        SymbolManager.getManager().addSymbol(getInt);
        super.visit();
        SymbolManager.getManager().leaveBlock();
    }

    @Override
    public Value genIR() {
        SymbolManager.getManager().setGlobal(true);
        SymbolManager.getManager().enterBlock();
        Symbol getInt = createGetInt();
        SymbolManager.getManager().addSymbol(getInt);
        super.genIR();
        SymbolManager.getManager().leaveBlock();
        return null;
    }
}
