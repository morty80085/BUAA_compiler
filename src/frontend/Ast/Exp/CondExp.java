package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.BasicBlock;

import java.util.ArrayList;

public class CondExp extends Node {
    public CondExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    public void genIRForCond(BasicBlock thenBlock, BasicBlock elseBlock) {
        ((LOrExp) children.get(0)).genIRForLor(thenBlock, elseBlock);
    }
}
