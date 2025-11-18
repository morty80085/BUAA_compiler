package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.BasicBlock;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.BranchInstr;

import java.util.ArrayList;
//LAndExp → EqExp | LAndExp '&&' EqExp
public class LAndExp extends Node{
    public LAndExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    // 如果 exp1 为 false，exp2 不会被求值
//if (exp1 && exp2 && exp3) {
//         then block
//    } else {
//         else block
//    }

//    ; 第一个条件 a
//%cmp1 = icmp ne i32 %a, 0
//    br i1 %cmp1, label %next1, label %else_block
//
//    next1:
//    ; 第二个条件 b
//  %cmp2 = icmp ne i32 %b, 0
//    br i1 %cmp2, label %next2, label %else_block
//
//    next2:
//    ; 第三个条件 c (最后一个)
//  %cmp3 = icmp ne i32 %c, 0
//    br i1 %cmp3, label %then_block, label %else_block
//
//    then_block:
//    ; 所有条件都为真
//  ...
//
//    else_block:
//    ; 任一条件为假
//  ...

    public void genIRForLAnd(BasicBlock thenBlock, BasicBlock elseBlock) {
        for(int i = 0; i < children.size(); i++) {
            if(children.get(i) instanceof EqExp) {
                if(i == children.size() - 1) {
                    Value cond = children.get(i).genIR();
                    Instr instr = new BranchInstr(IRBuilder.getInstance().genVarName(), cond, thenBlock, elseBlock);
                } else {
                    BasicBlock basicBlock = new BasicBlock(IRBuilder.getInstance().genBBName());
                    Value cond = children.get(i).genIR();
                    Instr instr = new BranchInstr((IRBuilder.getInstance().genVarName()), cond, basicBlock, elseBlock);
                    IRBuilder.getInstance().setCurBasicBlock(basicBlock);
                }
            }
        }
    }
}
