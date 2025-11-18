package frontend;

import java.util.ArrayList;

public class TokenNode extends Node{
    private Token token;

    public TokenNode(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children,Token token) {
        super(startLine,endLine,syntaxVarType,children);
        this.token = token;
    }

    public Token getToken() {
        return this.token;
    }
}
