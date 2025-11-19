package frontend.Ast.Var;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Exp.ConstExp;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.symbol.ConstSymbol;

import java.util.ArrayList;
import frontend.*;
import frontend.symbol.SymbolManager;
import frontend.symbol.SymbolType;
import frontend.symbol.ValueType;
import llvm_ir.*;
import llvm_ir.instr.AllocaInstr;
import llvm_ir.instr.GepInstr;
import llvm_ir.instr.StoreInstr;
import llvm_ir.type.ArrayType;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

public class ConstDef extends Node{
    private ConstSymbol symbol;

    public ConstDef(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public ConstSymbol createSymbol() {
        // ConstDef ==> Indent {'[' ConstExp ']'} '=' ConstInitVal
        String symbolName = ((TokenNode) children.get(0)).getToken().getTokenContent();
        SymbolType symbolType = SymbolType.ConstInt;
        ValueType valueType = ValueType.INT;
        boolean isArray = false;
        int len = 0;
        int num = children.size();

        for(Node child : children) {
            if(child instanceof ConstExp) {
                symbolType = SymbolType.ConstIntArray;
                isArray = true;
                //使用execute计算数组长度
                len = child.execute();
            }
        }

        Initial initial = null;
        LLvmType lLvmType = null;
        if(isArray) {
            lLvmType = new ArrayType(len, BaseType.INT32);
        } else {
            lLvmType = BaseType.INT32;
        }

        //获取初始值
        if (children.get(num-1).getSyntaxVarType() != SyntaxVarType.CONST_INITVAL) {
            initial = new Initial(lLvmType, null);
        } else {
            int dim = 0;
            if(isArray) {
                dim = 1;
            }
            ArrayList<Integer> values = ((ConstInitVal)children.get(num-1)).execute(dim);
            if(values.size() < len) {
                int valueSize = len - values.size();
                for(int i = 0; i < valueSize; i++) {
                    values.add(0);
                }
            }
            initial = new Initial(lLvmType, values);
        }

        return new ConstSymbol(symbolName, symbolType, valueType, isArray, len, initial);
    }

    @Override
    public void visit() {
        this.symbol = createSymbol();
        super.visit();
        //Error b
        boolean abs = SymbolManager.getManager().addSymbol(this.symbol);
        if(!abs) {
            ErrorRecorder.addError(new Error(Error.ErrorType.b, children.get(0).getEndLine()));
        }
    }

    @Override
    public Value genIR() {
        SymbolManager.getManager().addSymbol(symbol);
        Initial initial = symbol.getInitial();
        if(symbol.getGlobal()) {
            //如果是全局变量
            String name = IRBuilder.getInstance().genGlobalVarName();
            GlobalVar globalVar = new GlobalVar(new PointerType(initial.getType()), name, initial);
            symbol.setlLvmValue(globalVar);
        } else {
            //如果是变量
            Instr instr = null;
            if(symbol.isArray()) {
                //如果是数组
                LLvmType lLvmType = new ArrayType(symbol.getLen(), BaseType.INT32);
                String name = IRBuilder.getInstance().genVarName();
                instr = new AllocaInstr(name, lLvmType);
                symbol.setlLvmValue(instr);
                //生成gep和store指令
                Value value = instr;
                int offset = 0;
                for(Integer num : initial.getValues()) {
                    instr = new GepInstr(IRBuilder.getInstance().genVarName(), value, new Constant(offset));
                    instr = new StoreInstr(IRBuilder.getInstance().genVarName(), new Constant(num), instr);
                    offset++;
                }
            } else {
                //非数组类型
                LLvmType lLvmType = BaseType.INT32;
                String name = IRBuilder.getInstance().genVarName();
                instr = new AllocaInstr(name, lLvmType);
                symbol.setlLvmValue(instr);
                //生成store指令
                int value = initial.getValues().get(0);
                name = IRBuilder.getInstance().genVarName();
                instr = new StoreInstr(name, new Constant(value), instr);
            }
        }
        return null;
    }
}
