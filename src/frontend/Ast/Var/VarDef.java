package frontend.Ast.Var;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Exp.ConstExp;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.symbol.*;
import llvm_ir.*;
import llvm_ir.instr.AllocaInstr;
import llvm_ir.instr.GepInstr;
import llvm_ir.instr.StoreInstr;
import llvm_ir.type.ArrayType;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

import java.util.ArrayList;

public class VarDef extends Node{
    private VarSymbol varSymbol = null;
    private StaticSymbol staticSymbol = null;

    public VarDef(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public StaticSymbol createStaticSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getToken().getTokenContent();
        SymbolType symbolType = SymbolType.StaticInt;
        ValueType valueType = ValueType.INT;
        boolean isArray = false;
        int len = 0;

        for(Node child : children) {
            if(child instanceof ConstExp) {
                symbolType = SymbolType.StaticIntArray;
                isArray = true;
                //先使用1填位
                len = child.execute();
            }
        }
        //static 变量一定可以求出初值
        Initial initial = null;
        LLvmType lLvmType = null;
        if(isArray) {
            lLvmType = new ArrayType(len, BaseType.INT32);
        } else {
            lLvmType = BaseType.INT32;
        }
        if(children.get(children.size() - 1 ).getSyntaxVarType() != SyntaxVarType.INIT_VAL) {
            initial = new Initial(lLvmType, null);
        } else {
            int dim = 0;
            if(isArray) {
                dim = 1;
            }
            ArrayList<Integer> values = ((InitVal)children.get(children.size() - 1)).execute(dim);
            if(values.size() < len) {
                int valueSize = len - values.size();
                for(int i = 0; i < valueSize; i++) {
                    values.add(0);
                }
            }
            initial = new Initial(lLvmType, values);
        }

        return new StaticSymbol(symbolName, symbolType, valueType, isArray, len, initial);
    }

    public VarSymbol createVarSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getToken().getTokenContent();
        SymbolType symbolType = SymbolType.Int;
        ValueType valueType = ValueType.INT;
        boolean isArray = false;
        int len = 0;

        for(Node child : children) {
            if(child instanceof ConstExp) {
                symbolType = SymbolType.IntArray;
                isArray = true;
                //计算数组长度
                len = child.execute();
            }
        }

        //全局变量可以求出值
        if(SymbolManager.getManager().getGlobal()) {
            Initial initial = null;
            LLvmType lLvmType =  null;
            if(isArray) {
                lLvmType = new ArrayType(len, BaseType.INT32);
            } else {
                lLvmType = BaseType.INT32;
            }
            if (children.get(children.size() - 1 ).getSyntaxVarType() != SyntaxVarType.INIT_VAL) {
                initial = new Initial(lLvmType, null);
            } else {
                int dim = 0;
                if(isArray) {
                    dim = 1;
                }
                ArrayList<Integer> values = ((InitVal)children.get(children.size() - 1)).execute(dim);
                if(values.size() < len) {
                    int valueSize = len - values.size();
                    for(int i = 0; i < valueSize; i++) {
                        values.add(0);
                    }
                }
                initial = new Initial(lLvmType, values);
            }
            return new VarSymbol(symbolName, symbolType, valueType, isArray, len, initial);
        }

        return new VarSymbol(symbolName, symbolType, valueType, isArray, len);
    }

    @Override
    public void visit() {
        if(SymbolManager.getManager().getStatic()) {
            this.staticSymbol = createStaticSymbol();
            super.visit();
            //Error b
            boolean abs = SymbolManager.getManager().addSymbol(staticSymbol);
            if(!abs) {
                ErrorRecorder.addError(new Error(Error.ErrorType.b, children.get(0).getEndLine()));
            }
        } else {
            this.varSymbol = createVarSymbol();
            super.visit();
            //Error b
            boolean abs = SymbolManager.getManager().addSymbol(varSymbol);
            if(!abs) {
                ErrorRecorder.addError(new Error(Error.ErrorType.b, children.get(0).getEndLine()));
            }
        }
    }

    @Override
    public Value genIR() {
        if(this.staticSymbol != null) {
            //这是一个静态变量，一定有初值
            SymbolManager.getManager().addSymbol(staticSymbol);
            Initial initial = staticSymbol.getInitial();
            String name = IRBuilder.getInstance().genStaticVarName();
            StaticVar staticVar = new StaticVar(new PointerType(initial.getType()), name, initial);
            staticSymbol.setlLvmValue(staticVar);
        } else {
            //这是一个变量
            if(varSymbol.getGlobal()) {
                //如果是全局变量
                SymbolManager.getManager().addSymbol(varSymbol);
                Initial initial = varSymbol.getInitial();
                String name = IRBuilder.getInstance().genGlobalVarName();
                GlobalVar globalVar = new GlobalVar(new PointerType(initial.getType()), name, initial);
                varSymbol.setlLvmValue(globalVar);
            } else {
                //否则是局部变量
                SymbolManager.getManager().addSymbol(varSymbol);
                Instr instr = null;
                Initial initial = varSymbol.getInitial();
                if(varSymbol.isArray()) {
                    //如果是数组
                    LLvmType lLvmType = new ArrayType(varSymbol.getLen(), BaseType.INT32);
                    //alloca指令
                    instr = new AllocaInstr(IRBuilder.getInstance().genVarName(), lLvmType);
                    varSymbol.setlLvmValue(instr);
                    //gep + store
                    if(children.get(children.size() - 1) instanceof InitVal) {
                        //保存原来instr
                        Value pointer = instr;
                        InitVal initVal = (InitVal) children.get(children.size() - 1);
                        ArrayList<Value> values = initVal.genIRList(1);
                        int offset = 0;
                        for(Value value :values) {
                            instr = new GepInstr(IRBuilder.getInstance().genVarName(), pointer, new Constant(offset));
                            instr = new StoreInstr(IRBuilder.getInstance().genVarName(), value, instr);
                            offset++;

                        }
                    }
                } else {
                    //否则是变量
                    //alloca指令
                    LLvmType lLvmType = BaseType.INT32;
                    instr = new AllocaInstr(IRBuilder.getInstance().genVarName(), lLvmType);
                    varSymbol.setlLvmValue(instr);
                    //如果有初值，就使用赋值语句
                    if(children.get(children.size() - 1) instanceof InitVal) {
                        InitVal initVal = (InitVal) children.get(children.size() -1);
                        Value value = initVal.genIRList(0).get(0);
                        instr = new StoreInstr(IRBuilder.getInstance().genVarName(), value, instr);
                    }
                }
            }
        }
        return null;
    }
}
