package frontend.Ast.Func;

import error.Error;
import error.ErrorRecorder;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import frontend.symbol.SymbolManager;
import frontend.symbol.SymbolType;
import frontend.symbol.ValueType;
import frontend.symbol.VarSymbol;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Param;
import llvm_ir.Value;
import llvm_ir.instr.AllocaInstr;
import llvm_ir.instr.StoreInstr;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

import java.util.ArrayList;

public class FuncFParam extends Node{
    private VarSymbol varSymbol;

    public FuncFParam(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public VarSymbol createSymbol() {
        String symbolName = ((TokenNode)children.get(1)).getToken().getTokenContent();
        SymbolType symbolType = SymbolType.Int;
        ValueType valueType = ValueType.INT;
        boolean isArray = false;
        //长度未知，使用1填位
        int len = 0;
        for(int i = 0; i < children.size(); i++) {
            if(children.get(i) instanceof TokenNode && ((TokenNode) children.get(i)).getToken().getTokenType() == TokenType.LBRACK) {
                symbolType = SymbolType.IntArray;
                isArray = true;
                //如果是数组，长度直接使用1占位（因为不用计算长度）
                len = 1;
            }
        }
        return new VarSymbol(symbolName,symbolType,valueType,isArray,len);
    }

    public ValueType getValueType() {
        return varSymbol.getValueType();
    }

    public int getFuncFDim() {
        if(varSymbol.isArray()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void visit() {
        this.varSymbol = createSymbol();
        //Error b
        boolean abs = SymbolManager.getManager().addSymbol(varSymbol);
        if(!abs) {
            ErrorRecorder.addError(new Error(Error.ErrorType.b, children.get(1).getEndLine()));
        }
        super.visit();
    }

//    int foo(int a, int b){
//        return a + b;
//    }

//    define dso_local i32 @foo(i32 %0, i32 %1) {
//        %3 = alloca i32
//        %4 = alloca i32
//        store i32 %0, i32* %3
//        store i32 %1, i32* %4
//        %5 = load i32, i32* %3
//        %6 = load i32, i32* %4
//        %7 = add nsw i32 %5, %6
//        ret i32 %7
//    }
    @Override
    public Value genIR() {
        SymbolManager.getManager().addSymbol(varSymbol);
        LLvmType lLvmType = null;
        if(varSymbol.isArray()) {
            lLvmType = new PointerType(BaseType.INT32);
        } else {
            lLvmType = BaseType.INT32;
        }
        //创建参数
        Param param = new Param(lLvmType, IRBuilder.getInstance().genParamName());
        //如果是变量，要有存储单元，防止原值被修改，如果是形参就不用
        if(param.getType().isInt32()) {
            Instr instr = new AllocaInstr(IRBuilder.getInstance().genVarName(), lLvmType);
            varSymbol.setlLvmValue(instr);
            instr = new StoreInstr(IRBuilder.getInstance().genVarName(), param, instr);
        } else {
            varSymbol.setlLvmValue(param);
        }
        super.genIR();
        return null;
    }

}
