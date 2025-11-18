package frontend.symbol;

import java.util.*;

public class SymbolManager {
    //单例模式
    private static final SymbolManager manager = new SymbolManager();
    //符号表
    private Stack<SymbolTable> symbolTables;
    //作用域表
    private Stack<Integer> symbolScope;
    //定义过String的符号表
    private HashMap<String, Stack<SymbolTable>> symbolMap;
    //倒数的Func,记录是什么类型
    private FuncSymbol latestFunc;
    //作用域编号
    private int scopeNumber;
    //循环的层数
    private int loopDepth;
    private boolean isGlobal;
    private boolean isStatic;
    private HashMap<Integer, LinkedHashSet<Symbol>> symbolToPrint;

    public SymbolManager() {
        this.symbolTables = new Stack<>();
        this.symbolScope = new Stack<>();
        this.symbolMap = new HashMap<>();
        this.latestFunc = null;
        this.scopeNumber = 0;
        this.loopDepth = 0;
        this.isGlobal = true;
        this.isStatic = false;
        this.symbolToPrint = new HashMap<>();
    }

    public static SymbolManager getManager() {
        return manager;
    }

    public boolean addSymbol(Symbol symbol) {
        SymbolTable topTable = this.symbolTables.peek();
        //目前作用域是否有定义
        if(topTable.getSymbolByName(symbol.getSymbolName()) != null) {
            return false;
        }
        //插入symbol
        topTable.addSymbol(symbol);
        //更新symbolMap
        symbolMap.compute(symbol.getSymbolName(),(k, v) -> {
            if(v == null) {
                v = new Stack<>();
            }
            v.add(topTable);
            return v;
        });
        return true;
    }

    public Symbol getSymbolByName(String name) {
        if(symbolMap.get(name) == null || symbolMap.get(name).isEmpty()) {
            return null;
        }
        SymbolTable symbolTable = symbolMap.get(name).peek();
        return symbolTable.getSymbolByName(name);
    }

    public void enterBlock() {
        //加入新的作用域
        SymbolTable symbolTable = new SymbolTable();
        this.symbolTables.push(symbolTable);
        //更新作用域编号
        scopeNumber = scopeNumber + 1;
        this.symbolScope.push(scopeNumber);
    }

    public void leaveBlock() {
        //弹出要离开的作用域
        SymbolTable symbolTable = this.symbolTables.pop();
        //将作用域编号弹出
        int scopeLeave = symbolScope.pop();
        //更新symbolMap
        for(String name : symbolTable.getNames()) {
            symbolMap.get(name).pop();
        }
        //将需要输出的symbol存到输出中
        symbolToPrint.put(scopeLeave, new LinkedHashSet<>());
        for(Symbol symbol : symbolTable.getSymbols()) {
            symbolToPrint.get(scopeLeave).add(symbol);
        }
    }

    public void enterFunc(FuncSymbol symbol) {
        this.latestFunc = symbol;
        enterBlock();
    }

    public void leaveFunc() {
        this.latestFunc = null;
        leaveBlock();
    }

    public FuncSymbol getLatestFunc() {
        return this.latestFunc;
    }

    public void enterLoop() {
        this.loopDepth++;
    }

    public void leaveLoop() {
        this.loopDepth--;
    }

    public int getLoopDepth() {
        return this.loopDepth;
    }

    public boolean getGlobal() {
        return this.isGlobal;
    }

    public void setGlobal(boolean global) {
        this.isGlobal = global;
    }

    public boolean getStatic() {
        return this.isStatic;
    }

    public void setStatic(boolean Static) {
        this.isStatic = Static;
    }

    public int getScopeNumber() {
        return symbolScope.peek();
    }

    public HashMap<Integer, LinkedHashSet<Symbol>> getSymbolToPrint(){
        return this.symbolToPrint;
    }

}
