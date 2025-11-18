package frontend;

import llvm_ir.Value;

import java.util.ArrayList;

public class Node {
    protected int startLine;
    protected int endLine;
    protected SyntaxVarType syntaxVarType;
    protected ArrayList<Node> children;

    public Node(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.syntaxVarType = syntaxVarType;
        this.children = children;
    }

    public int getStartLine() {
        return this.startLine;
    }

    public int getEndLine() {
        return this.endLine;
    }

    public SyntaxVarType getSyntaxVarType() {
        return this.syntaxVarType;
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    //用于判断是否是数组
    public Integer getDim() {
        return null;
    }

    public void visit() {
        if(children == null) {
            return;
        }
        for(Node child : children) {
            child.visit();
        }
    }

    //计算初值
    public int execute() {
        return 0;
    }

    //生成中间代码,运用一切皆Value,实际上是生成一个llvm ir的语法树。
    public Value genIR() {
        if(children == null) {
            return null;
        } else {
            for(Node node : children) {
                node.genIR();
            }
        }
        return null;
    }

}
