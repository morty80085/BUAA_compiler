package frontend.Ast.Stmt;

import frontend.Ast.Exp.CondExp;
import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.BasicBlock;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.JumpInstr;

import java.util.ArrayList;

public class IfStmt extends Node{
    public IfStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public Value genIR() {
        boolean hasElse = false;
        if(children.size() > 5) {
            hasElse = true;
        }
        if(hasElse) {
            BasicBlock thenBlock = new BasicBlock(IRBuilder.getInstance().genBBName());
            BasicBlock followBlock = new BasicBlock(IRBuilder.getInstance().genBBName());
            BasicBlock elseBlock = new BasicBlock(IRBuilder.getInstance().genBBName());
            ((CondExp) children.get(2)).genIRForCond(thenBlock, elseBlock);
            //先解析then
            IRBuilder.getInstance().setCurBasicBlock(thenBlock);
            children.get(4).genIR();
            Instr instr = new JumpInstr(IRBuilder.getInstance().genVarName(), followBlock);
            //再解析else
            IRBuilder.getInstance().setCurBasicBlock(elseBlock);
            children.get(6).genIR();
            instr = new JumpInstr(IRBuilder.getInstance().genVarName(), followBlock);
            //最后解析follow
            IRBuilder.getInstance().setCurBasicBlock(thenBlock);
        } else {
            BasicBlock thenBlock = new BasicBlock(IRBuilder.getInstance().genBBName());
            BasicBlock followBlock = new BasicBlock(IRBuilder.getInstance().genBBName());
            ((CondExp) children.get(2)).genIRForCond(thenBlock, followBlock);
            //先解析then,再解析follow
            IRBuilder.getInstance().setCurBasicBlock(thenBlock);
            children.get(4).genIR();
            Instr instr = new JumpInstr(IRBuilder.getInstance().genVarName(), followBlock);
            //解析follow
            IRBuilder.getInstance().setCurBasicBlock(followBlock);
        }
        return null;
    }
}
