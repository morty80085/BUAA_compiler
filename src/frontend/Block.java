package frontend;

import java.util.ArrayList;

public class Block extends Node{
    public Block(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }
}
