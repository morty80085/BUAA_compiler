package frontend;

import frontend.symbol.*;
import llvm_ir.Function;
import llvm_ir.IRBuilder;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;

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
        //将getint函数的定义也放入module中方便后续调用，但最后不会输出getint函数
        String name = IRBuilder.getInstance().genFunctionName(getInt.getSymbolName());
        LLvmType lLvmType = BaseType.INT32;
        //生成function插入到module中
        Function function = new Function(name, lLvmType);
        ((FuncSymbol)getInt).setlLvmValue(function);
        //开始生成代码
        super.genIR();
        SymbolManager.getManager().leaveBlock();
        return null;
    }
}
