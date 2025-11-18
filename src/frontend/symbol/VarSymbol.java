package frontend.symbol;

import llvm_ir.Initial;
import llvm_ir.Value;
import llvm_ir.type.LLvmType;

public class VarSymbol extends Symbol{
    private ValueType valueType;
    //是否是数组
    private boolean isArray;
    //如果是数组，数组的长度；
    private int len;
    private boolean isGlobal;

    //用于代码生成
    private Initial initial;
    private Value lLvmValue;

    public VarSymbol(String symbolName,SymbolType symbolType,
                        ValueType valueType, boolean isArray, int len) {
        super(symbolName, symbolType, SymbolManager.getManager().getScopeNumber());
        this.valueType = valueType;
        this.isArray = isArray;
        this.len = len;
        this.isGlobal = SymbolManager.getManager().getGlobal();
        this.initial = null;
        this.lLvmValue = null;
    }

    public VarSymbol(String symbolName,SymbolType symbolType,
                     ValueType valueType, boolean isArray, int len, Initial initial) {
        super(symbolName, symbolType, SymbolManager.getManager().getScopeNumber());
        this.valueType = valueType;
        this.isArray = isArray;
        this.len = len;
        this.isGlobal = SymbolManager.getManager().getGlobal();
        this.initial = initial;
        this.lLvmValue = null;
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public boolean isArray() {
        return this.isArray;
    }

    public int getLen() {
        return len;
    }

    public boolean getGlobal() {
        return this.isGlobal;
    }

    public Initial getInitial() {
        return this.initial;
    }

    //常数
    public int getIntValue() {
        return initial.getValues().get(0);
    }

    //数组
    public int getIntValue(int index) {
        return initial.getValues().get(index);
    }

    public void setlLvmValue(Value lLvmValue) {
        this.lLvmValue = lLvmValue;
    }

    public Value getlLvmValue() {
        return this.lLvmValue;
    }
}
