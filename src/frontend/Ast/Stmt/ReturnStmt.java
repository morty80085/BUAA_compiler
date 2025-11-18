package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Node;
import frontend.Ast.Exp.Exp;
import frontend.SyntaxVarType;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolManager;
import frontend.symbol.ValueType;
import llvm_ir.Constant;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.ReturnInstr;
import llvm_ir.type.BaseType;

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

    @Override
    public Value genIR() {
        // 只有一条return时，如果函数返回类型为int，那么我们就翻译成“ret i32 0”, 反之就翻译成“ret void”;
        Instr instr = null;
        Value retValue = null;
        if (children.get(1) instanceof Exp) {
            retValue = children.get(1).genIR();
        } else if (IRBuilder.getInstance().getCurFunction().getRetType() == BaseType.INT32) {
            //默认int函数的return为0
            retValue = new Constant(0);
        }
        instr = new ReturnInstr(IRBuilder.getInstance().genVarName(), retValue);
        return instr;
    }
}
