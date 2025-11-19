package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.symbol.SymbolManager;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.JumpInstr;

import java.util.ArrayList;

public class ContinueStmt extends Node{
    public ContinueStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public void visit() {
        //Error m
        if(SymbolManager.getManager().getLoopDepth() <= 0) {
            ErrorRecorder.addError(new Error(Error.ErrorType.m, endLine));
        }
        super.visit();
    }

    @Override
    public Value genIR() {
        Instr instr = new JumpInstr(IRBuilder.getInstance().genVarName(), IRBuilder.getInstance().getCurLoop().getForStmt2());
        return null;
    }
}
