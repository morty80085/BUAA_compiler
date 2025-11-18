package frontend;

public class Token {
    private final String tokenContent;
    private final TokenType tokenType;
    private final int lineNum;

    public Token(String tokenContent, TokenType tokenType, int lineNum) {
        this.tokenContent = tokenContent;
        this.tokenType = tokenType;
        this.lineNum = lineNum;
    }

    public String getTokenContent() {
        return this.tokenContent;
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public int getLineNum() {
        return this.lineNum;
    }
}
