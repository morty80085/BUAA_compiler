package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.IcmpInstr;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;

import java.util.ArrayList;

//RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
public class RelExp extends Node{
    public RelExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Value genIR() {
        Value operand1 = children.get(0).genIR();
        Value operand2 = null;
        Value ans = null;
        if(children.size() == 1) {
            return operand1;
        } else {
            for(int i = 1; i < children.size(); i++) {
                Node child = children.get(i);
                if(child instanceof TokenNode) {
                    //如果上一条是icmp指令，要进行扩展
                    if (!operand1.getType().isInt32()) {
                        operand1 = new ZextInstr(IRBuilder.getInstance().genVarName(), operand1, BaseType.INT32);
                    }
                    //AddExp返回的是32位
                    operand2 = children.get(i + 1).genIR();
                    if(((TokenNode) child).getToken().getTokenType() == TokenType.LSS) {
                        // <
                        ans = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.slt, operand1, operand2);
                    } else if(((TokenNode) child).getToken().getTokenType() == TokenType.LEQ) {
                        // <=
                        ans = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.sle, operand1, operand2);
                    } else if(((TokenNode) child).getToken().getTokenType() == TokenType.GRE) {
                        // >
                        ans = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.sgt, operand1, operand2);
                    } else {
                        // >=
                        ans = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.sge, operand1, operand2);
                    }
                }
                operand1 = ans;
            }
            //注意返回的类型是INT1
            return operand1;
        }
    }
}
