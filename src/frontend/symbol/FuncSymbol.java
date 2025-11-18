package frontend.symbol;

import llvm_ir.Function;
import llvm_ir.Value;

import java.util.ArrayList;

public class FuncSymbol extends Symbol{
    private ValueType returnType;
    //函数形参的valueType
    private ArrayList<ValueType> FuncFParamsType;
    //函数形参是否是数组，是：数组的长度；否：0
    private ArrayList<Integer> FuncFDims;
    private Function lLvmValue;

    public FuncSymbol(String symbolName, SymbolType symbolType,
                      ValueType returnType, ArrayList<ValueType> FuncFParamsType, ArrayList<Integer> FuncFDims) {
        super(symbolName, symbolType, SymbolManager.getManager().getScopeNumber());
        this.returnType = returnType;
        this.FuncFParamsType = FuncFParamsType;
        this.FuncFDims = FuncFDims;
        this.lLvmValue = null;
    }

    public FuncSymbol(String symbolName, SymbolType symbolType, ValueType returnType) {
        super(symbolName, symbolType, SymbolManager.getManager().getScopeNumber());
        this.returnType = returnType;
    }

    public ValueType getReturnType() {
        return this.returnType;
    }

    public ArrayList<ValueType> getFuncFParamsType() {
        return this.FuncFParamsType;
    }

    public ArrayList<Integer> getFuncFDims() {
        return this.FuncFDims;
    }

    public void setFuncInfo(ArrayList<ValueType> funcFParamsTypes, ArrayList<Integer> funcFDims) {
        this.FuncFParamsType = funcFParamsTypes;
        this.FuncFDims = funcFDims;
    }

    public void setlLvmValue(Function lLvmValue) {
        this.lLvmValue = lLvmValue;
    }

    public Function getLLvmValue() {
        return this.lLvmValue;
    }
}
