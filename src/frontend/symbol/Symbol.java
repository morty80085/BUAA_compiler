package frontend.symbol;

public class Symbol {
    private String symbolName;
    private SymbolType symbolType;
    private int scope;

    public Symbol(String symbolName, SymbolType symbolType, int scope) {
        this.symbolName = symbolName;
        this.symbolType = symbolType;
        this.scope = scope;
    }

    public String getSymbolName() {
        return this.symbolName;
    }

    public SymbolType getSymbolType() {
        return this.symbolType;
    }

    public int getScope() {
        return this.scope;
    }
}
