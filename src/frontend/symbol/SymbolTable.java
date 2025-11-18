package frontend.symbol;

import java.util.LinkedHashSet;
import java.util.Iterator;

public class SymbolTable {
    private LinkedHashSet<Symbol> symbols;

    public SymbolTable() {
        this.symbols = new LinkedHashSet<>();
    }

    public Symbol getSymbolByName(String symbolName) {
        for(Symbol symbol : symbols) {
            if(symbol.getSymbolName().equals(symbolName)) {
                return symbol;
            }
        }
        return null;
    }

    public LinkedHashSet<String> getNames() {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        for(Symbol symbol : symbols) {
            names.add(symbol.getSymbolName());
        }
        return names;
    }

    public LinkedHashSet<Symbol> getSymbols() {
        LinkedHashSet<Symbol> symbolsCopy = new LinkedHashSet<>();
        for(Symbol symbol : symbols) {
            symbolsCopy.add(symbol);
        }
        return symbolsCopy;
    }

    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
    }
}
