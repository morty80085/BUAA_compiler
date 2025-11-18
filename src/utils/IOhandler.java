package utils;

import error.Error;
import error.ErrorRecorder;
import frontend.FrontEnd;
import frontend.SyntaxVarType;
import frontend.Token;
import frontend.TokenType;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolManager;
import frontend.symbol.SymbolType;
import llvm_ir.Module;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class IOhandler {
    public static boolean onOff = true;

    private static PushbackInputStream inputStream = null;
    private static OutputStream tokenStream = null;
    private static OutputStream errorStream = null;
    private static OutputStream parserStream = null;
    private static OutputStream symbolStream = null;
    private static OutputStream llvmStream = null;


    public static void setIOhandler() throws FileNotFoundException {
        IOhandler.inputStream = new PushbackInputStream(new FileInputStream("testfile.txt"), 16);
        IOhandler.tokenStream = new FileOutputStream("lexer.txt");
        IOhandler.errorStream = new FileOutputStream("error.txt");
        IOhandler.parserStream = new FileOutputStream("parser.txt");
        IOhandler.symbolStream = new FileOutputStream("symbol.txt");
        IOhandler.llvmStream = new FileOutputStream("llvm_out.txt");
    }

    public static PushbackInputStream getInputStream() {
        return inputStream;
    }

    public static void lexerOutput() throws IOException {
        if(ErrorRecorder.isEmpty()) {
            for(Token token : FrontEnd.getLexer().getTokenList()) {
                if(token.getTokenType() != TokenType.EOF && token.getTokenType() != TokenType.ERROR) {
                    String string = token.getTokenType() + " " + token.getTokenContent() + "\n";
                    tokenStream.write(string.getBytes());
                }
            }
        } else {
            for(Error error : ErrorRecorder.getErrorRecorder()) {
                String string = error.getLineNum() + " " + error.getErrorType() + "\n";
                errorStream.write(string.getBytes());
            }
        }
    }

    public static void errorOutput() throws IOException {
        ErrorRecorder.sortByLineNum();

        /*for(Error error : ErrorRecorder.getErrorRecorder()) {
            String string = error.getLineNum() + " " + error.getErrorType() + "\n";
            System.out.println(string);
        }*/

        for(Error error : ErrorRecorder.getErrorRecorder()) {
            String string = error.getLineNum() + " " + error.getErrorType() + "\n";
            errorStream.write(string.getBytes());
        }
    }

    public static void printToken(Token token) {
        String content = token.getTokenType() + " " + token.getTokenContent() + "\n";
        if(onOff) {
            try {
                parserStream.write(content.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void printSyntaxVarType(SyntaxVarType type) {
        String content = "<" + type.toString() + ">\n";
        if(onOff) {
            try {
                parserStream.write(content.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void printSymbol() {
        HashMap<Integer, LinkedHashSet<Symbol>> symbolMap = SymbolManager.getManager().getSymbolToPrint();
        List<Integer> keys = new ArrayList<>(symbolMap.keySet());
        Collections.sort(keys);

        for (Integer key : keys) {
            LinkedHashSet<Symbol> symbols = symbolMap.get(key);
            for (Symbol s : symbols) {
                if (s.getSymbolType() != SymbolType.GetIntFunc && s.getSymbolType() != SymbolType.MainFunc) {
                    String content = s.getScope() + " " + s.getSymbolName() + " " + s.getSymbolType() + "\n";
                    if (onOff) {
                        try {
                            symbolStream.write(content.getBytes());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

    }

    public static void printLLVM(Module module) {
        try {
            llvmStream.write(module.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
