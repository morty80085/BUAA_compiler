package frontend.Ast.Exp;

import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.BasicBlock;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.BranchInstr;

import java.util.ArrayList;

//LOrExp → LAndExp | LOrExp '||' LAndExp
public class LOrExp extends Node{
    public LOrExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    public void genIRForLor(BasicBlock thenBlock, BasicBlock elseBlock) {
        for(int i = 0; i < children.size(); i++) {
            if(children.get(i) instanceof LAndExp) {
                if(i == children.size() - 1) {
                    //直接调用Land语句完成判断=》== 1 ? thenBlock : elseBlock
                    ((LAndExp) children.get(i)).genIRForLAnd(thenBlock, elseBlock);
                } else {
                    BasicBlock basicBlock = new BasicBlock(IRBuilder.getInstance().genBBName());
                    //如何得到1就跳转到对应的块，否则再做下一次判断
                    ((LAndExp) children.get(i)).genIRForLAnd(thenBlock, basicBlock);
                    IRBuilder.getInstance().setCurBasicBlock(basicBlock);
                }
            }
        }
    }
}
