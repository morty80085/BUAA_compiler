package frontend.Ast.Stmt;

import frontend.Node;
import frontend.SyntaxVarType;
import frontend.symbol.SymbolManager;
import llvm_ir.Value;

import java.util.ArrayList;

public class BlockStmt extends Node{
    public BlockStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public void visit() {
        SymbolManager.getManager().enterBlock();
        super.visit();
        SymbolManager.getManager().leaveBlock();
    }

    @Override
    public Value genIR() {
        SymbolManager.getManager().enterBlock();
        super.genIR();
        SymbolManager.getManager().leaveBlock();
        return null;
    }
}
