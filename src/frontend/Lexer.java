package frontend;

import error.Error;
import error.ErrorRecorder;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;

public class Lexer {
    private final PushbackInputStream reader;
    private final ArrayList<Token> tokenList;
    private char currentChar;
    private int lineNum;

    public Lexer(PushbackInputStream reader) throws IOException {
        this.reader = reader;
        this.tokenList = new ArrayList<>();
        this.currentChar = (char) reader.read();
        this.lineNum = 1;
    }

    public void generateTokenList() throws IOException{
        Token token = getToken();
        while(token.getTokenType() != TokenType.EOF) {
            tokenList.add(token);
            token = getToken();
        }
        tokenList.add(token);
    }

    public ArrayList<Token> getTokenList() {
        return this.tokenList;
    }

    public ArrayList<Token> getNoneErrorTokenList() {
        ArrayList<Token> tokens = new ArrayList<>();
        for(Token token: tokenList) {
            if(token.getTokenType() != TokenType.ERROR) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    public void Read() throws IOException {
        this.currentChar = (char) reader.read();
    }

    public Token getToken() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        skipBlank();
        if(isEOF()) {
            //System.out.println("EOF");
            return new Token("\uFFFF",TokenType.EOF,lineNum);
        } else if(isDigit()) {
            //System.out.println("Digit");
            return generateNum(stringBuilder);
        } else if(isUnder() || isLetter()) {
            //System.out.println("Letter");
            return generateIdent(stringBuilder);
        } else if(isDoubleQuote()) {
            //System.out.println("DoubleQuote");
            return generateString(stringBuilder);
        } else if(isSingleOp()) {
            //System.out.println("SingleOp");
            return generateSingleOp(stringBuilder);
        } else if(isDoubleOp()) {
            //System.out.println("DoubleOp");
            return generateDoubleOp(stringBuilder);
        } else {
            //System.out.println("Error");
            Read();
            return new Token(" ",TokenType.ERROR,lineNum);
        }
    }

    public void skipBlank() throws IOException {
        while(isBlank()) {
            if(isNewLine()) {
                lineNum++;
            }
            Read();
        }
    }

    public boolean isBlank() throws IOException {
        return currentChar == ' ' || currentChar == '\t' || currentChar == '\f' || currentChar == '\b' || isNewLine();
    }

    public boolean isNewLine() throws IOException {
        if(currentChar == '\r') {
            Read();
        }
        return currentChar == '\n' ;
    }

    public boolean isEOF() throws IOException{
        return currentChar == '\uFFFF';
    }

    public boolean isDigit()  {
        return Character.isDigit(currentChar);
    }

    public boolean isLetter() {
        return Character.isLetter(currentChar);
    }

    public boolean isUnder() {
        return currentChar == '_';
    }

    public boolean isDoubleQuote() {
        return currentChar == '"';
    }

    public boolean isSingleOp() {
        return currentChar == '+' || currentChar == '-' || currentChar == '*'  || currentChar == '%'
                || currentChar == ';' || currentChar == ',' || currentChar == '(' || currentChar == ')' || currentChar == '['
                || currentChar == ']' || currentChar == '{' || currentChar == '}';
    }

    public boolean isDoubleOp() {
        return currentChar == '!' || currentChar == '&' || currentChar == '|' || currentChar == '<' || currentChar == '>'
                || currentChar == '=' || currentChar == '/';
    }

    public Token generateNum(StringBuilder stringBuilder) throws IOException{
        while(isDigit()) {
            stringBuilder.append(currentChar);
            Read();
        }
        return new Token(stringBuilder.toString(),TokenType.INTCON,lineNum);
    }

    public Token generateIdent(StringBuilder stringBuilder) throws IOException {
        while(isLetter() || isUnder() || isDigit()) {
            stringBuilder.append(currentChar);
            Read();
        }
        return new Token(stringBuilder.toString(),getTokenType(stringBuilder.toString()),lineNum);
    }

    public Token generateString(StringBuilder stringBuilder) throws IOException {
        stringBuilder.append(currentChar);
        Read();
        while(!isDoubleQuote()) {
            stringBuilder.append(currentChar);
            Read();
        }
        stringBuilder.append(currentChar);
        Read();
        return new Token(stringBuilder.toString(),TokenType.STRCON,lineNum);
    }

    public Token generateSingleOp(StringBuilder stringBuilder) throws IOException {
        stringBuilder.append(currentChar);
        TokenType tokenType = getTokenType(currentChar);
        Read();
        return new Token(stringBuilder.toString(),tokenType,lineNum);
    }

    public Token generateDoubleOp(StringBuilder stringBuilder) throws IOException {
        if(currentChar == '&') {
            stringBuilder.append(currentChar);
            Read();
            if(currentChar == '&') {
                stringBuilder.append(currentChar);
                Read();
                return new Token(stringBuilder.toString(),TokenType.AND,lineNum);
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.a,lineNum));
                return new Token(stringBuilder.toString(),TokenType.AND,lineNum);
            }
        } else if(currentChar == '|') {
            stringBuilder.append(currentChar);
            Read();
            if(currentChar == '|') {
                stringBuilder.append(currentChar);
                Read();
                return new Token(stringBuilder.toString(),TokenType.OR,lineNum);
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.a,lineNum));
                return new Token(stringBuilder.toString(),TokenType.OR,lineNum);
            }
        } else if(currentChar == '<') {
            stringBuilder.append(currentChar);
            Read();
            if(currentChar == '=') {
                stringBuilder.append(currentChar);
                Read();
                return new Token(stringBuilder.toString(),TokenType.LEQ,lineNum);
            } else {
                return new Token(stringBuilder.toString(),TokenType.LSS,lineNum);
            }
        } else if(currentChar == '>') {
            stringBuilder.append(currentChar);
            Read();
            if(currentChar == '=') {
                stringBuilder.append(currentChar);
                Read();
                return new Token(stringBuilder.toString(),TokenType.GEQ,lineNum);
            } else {
                return new Token(stringBuilder.toString(),TokenType.GRE,lineNum);
            }
        } else if (currentChar == '!') {
            stringBuilder.append(currentChar);
            Read();
            if(currentChar == '=') {
                stringBuilder.append(currentChar);
                Read();
                return new Token(stringBuilder.toString(),TokenType.NEQ,lineNum);
            } else {
                return new Token(stringBuilder.toString(),TokenType.NOT,lineNum);
            }
        } else if (currentChar == '/') {
            stringBuilder.append(currentChar);
            Read();
            if(currentChar == '/') {
                while(!isNewLine() && !isEOF()) {
                    Read();
                }
                return new Token(stringBuilder.toString(),TokenType.ERROR,lineNum);
            } else if (currentChar == '*') {
                Read();
                while(true) {
                    if(currentChar == '*') {
                        Read();
                        if(currentChar == '/') {
                            Read();
                            break;
                        } else {
                            reader.unread(currentChar);
                        }
                    }
                    Read();
                }
                return new Token(stringBuilder.toString(),TokenType.ERROR,lineNum);
            } else {
                return new Token(stringBuilder.toString(),TokenType.DIV,lineNum);
            }
        } else {
            stringBuilder.append(currentChar);
            Read();
            if(currentChar == '=') {
                stringBuilder.append(currentChar);
                Read();
                return new Token(stringBuilder.toString(),TokenType.EQL,lineNum);
            } else {
                return new Token(stringBuilder.toString(),TokenType.ASSIGN,lineNum);
            }
        }
    }

    public TokenType getTokenType(String string) {
        switch (string) {
            case "const":
                return TokenType.CONSTTK;
            case "int":
                return TokenType.INTTK;
            case "static":
                return TokenType.STATICTK;
            case "break":
                return TokenType.BREAKTK;
            case "continue":
                return TokenType.CONTINUETK;
            case "if":
                return TokenType.IFTK;
            case "main":
                return TokenType.MAINTK;
            case "else":
                return TokenType.ELSETK;
            case "for":
                return TokenType.FORTK;
            case "return":
                return TokenType.RETURNTK;
            case "void":
                return TokenType.VOIDTK;
            case "printf":
                return TokenType.PRINTFTK;
            default:
                return TokenType.IDENFR;
        }
    }

    public TokenType getTokenType(char character) {
        switch (character) {
            case '!':
                return TokenType.NOT;
            case '&':
                return TokenType.AND;
            case '|':
                return TokenType.OR;
            case '+':
                return TokenType.PLUS;
            case '-':
                return TokenType.MINU;
            case '*':
                return TokenType.MULT;
            case '/':
                return TokenType.DIV;
            case '%':
                return TokenType.MOD;
            case '<':
                return TokenType.LSS;
            case '=':
                return TokenType.ASSIGN;
            case ';':
                return TokenType.SEMICN;
            case ',':
                return TokenType.COMMA;
            case '(':
                return TokenType.LPARENT;
            case ')':
                return TokenType.RPARENT;
            case '[':
                return TokenType.LBRACK;
            case ']':
                return TokenType.RBRACK;
            case '{':
                return TokenType.LBRACE;
            case '}':
                return TokenType.RBRACE;
            default:
                return TokenType.ERROR;
        }
    }
}
