package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import llvm_ir.Constant;
import llvm_ir.IRBuilder;
import llvm_ir.Value;
import llvm_ir.instr.IcmpInstr;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;

import java.util.ArrayList;
//EqExp → RelExp | EqExp ('==' | '!=') RelExp
public class EqExp extends Node{
    public EqExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Value genIR() {
        //保证返回的是INT1
        Value operand1 = children.get(0).genIR();
        Value operand2 = null;
        Value ans = null;
        if(children.size() == 1) {
            //只有RelExp(起始不用这一句，但为了保证一定正确)
            if(operand1.getType().isInt32()) {
                operand1 = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.ne, operand1,new Constant(0));
            }
            return operand1;
        } else {
            for(int i = 0; i < children.size(); i++) {
                if(children.get(i) instanceof TokenNode) {
                    //先拓展为32位，用于比较
                    if(!operand1.getType().isInt32()) {
                        operand1 = new ZextInstr(IRBuilder.getInstance().genVarName(), operand1, BaseType.INT32);
                    }
                    //获得两个INT32比较数
                    operand2 = children.get(i + 1).genIR();
                    if(!operand2.getType().isInt32()) {
                        operand2 = new ZextInstr(IRBuilder.getInstance().genVarName(), operand2, BaseType.INT32);
                    }
                    if(((TokenNode) children.get(i)).getToken().getTokenType() == TokenType.EQL) {
                        ans = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.eq, operand1, operand2);
                    } else {
                        ans = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.ne, operand1, operand2);
                    }
                    //准备用于下一次比较
                    operand1 = ans;
                }
            }
            return operand1;
        }
    }
}
