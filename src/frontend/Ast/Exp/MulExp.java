package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.AluInstr;

import java.util.ArrayList;

// MulExp ==> UnaryExp {('*' | '/' | '%') UnaryExp}
public class MulExp extends Node{
    public MulExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Integer getDim() {
        for(Node child : children) {
            if(child.getDim() != null) {
                return child.getDim();
            }
        }
        return null;
    }

    @Override
    public int execute() {
        int ans = children.get(0).execute();
        for(int i = 0; i < children.size(); i++) {
            if(children.get(i) instanceof TokenNode && (((TokenNode) children.get(i)).getToken().getTokenType()) == TokenType.MULT) {
                i = i + 1;
                ans = ans * children.get(i).execute();
            } else if(children.get(i) instanceof TokenNode && (((TokenNode) children.get(i)).getToken().getTokenType()) == TokenType.DIV) {
                i = i + 1;
                ans = ans / children.get(i).execute();
            } else {
                i = i + 1;
                ans = ans % children.get(i).execute();
            }
        }
        return ans;
    }

    @Override
    public Value genIR() {
        Value operand1 = children.get(0).genIR();
        Value operand2 = null;
        Instr instr = null;

        for(int i = 0; i < children.size(); i++) {
           if(children.get(i) instanceof TokenNode && (((TokenNode) children.get(i)).getToken().getTokenType()) == TokenType.MULT) {
               i = i + 1;
               operand2 = children.get(i).genIR();
               instr = new AluInstr(IRBuilder.getInstance().genVarName(), AluInstr.Op.mul, operand1, operand2);
               operand1 = instr;
           } else if(children.get(i) instanceof TokenNode && (((TokenNode) children.get(i)).getToken().getTokenType()) == TokenType.DIV) {
               i = i + 1;
               operand2 = children.get(i).genIR();
               instr = new AluInstr(IRBuilder.getInstance().genVarName(), AluInstr.Op.sdiv, operand1, operand2);
               operand1 = instr;
           } else {
               i = i + 1;
               operand2 = children.get(i).genIR();
               instr = new AluInstr(IRBuilder.getInstance().genVarName(), AluInstr.Op.srem, operand1, operand2);
               operand1 = instr;
           }
        }

        return operand1;
    }
}
