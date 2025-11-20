package frontend.Ast.Stmt;

import frontend.Ast.Exp.CondExp;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import frontend.symbol.SymbolManager;
import llvm_ir.*;
import llvm_ir.instr.JumpInstr;

import java.util.ArrayList;

public class ForStmtWhole extends Node {
    public ForStmtWhole(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    @Override
    public void visit() {
        SymbolManager.getManager().enterLoop();
        super.visit();
        SymbolManager.getManager().leaveLoop();
    }

    @Override
    public Value genIR() {
        SymbolManager.getManager().enterLoop();
        int SemicnCnt = 0;
        Instr instr = null;
        //五个基本Block
        BasicBlock forStmt1 = new BasicBlock(IRBuilder.getInstance().genBBName() + "forStmt1");
        BasicBlock condBlock = new BasicBlock(IRBuilder.getInstance().genBBName() + "forCond");
        BasicBlock forStmt2 = new BasicBlock(IRBuilder.getInstance().genBBName() + "forStmt2");
        BasicBlock loopBlock = new BasicBlock(IRBuilder.getInstance().genBBName() + "forLoop");
        BasicBlock followBlock = new BasicBlock(IRBuilder.getInstance().genBBName() + "forFollow");
        //新建Loop
        IRBuilder.getInstance().pushLoop(new Loop(condBlock, loopBlock, followBlock, forStmt1, forStmt2));
        //循环体开始，先到forStmt1
        instr = new JumpInstr(IRBuilder.getInstance().genVarName(), forStmt1);
        //先计算一共有多少个children
        int num = 5;
        for(Node child : children) {
            if(child instanceof ForStmt) {
                num = num + 1;
            } else if(child instanceof CondExp) {
                num = num + 1;
            }
        }
        for(int i = 0; i < children.size(); i++) {
            if(children.get(i) instanceof TokenNode && ((TokenNode) children.get(i)).getToken().getTokenType() == TokenType.SEMICN) {
                SemicnCnt = SemicnCnt + 1;
            } else if(children.get(i) instanceof ForStmt) {
                if(SemicnCnt == 0) {
                    //forStmt1
                    IRBuilder.getInstance().setCurBasicBlock(forStmt1);
                    ((ForStmt)children.get(i)).genIRList();
                    instr = new JumpInstr(IRBuilder.getInstance().genVarName(), condBlock);
                } else {
                    //forStmt2
                    IRBuilder.getInstance().setCurBasicBlock(forStmt2);
                    ((ForStmt) children.get(i)).genIRList();
                    instr = new JumpInstr(IRBuilder.getInstance().genVarName(), condBlock);
                }
            } else if(i == num) {
                //循环体
                IRBuilder.getInstance().setCurBasicBlock(loopBlock);
                children.get(i).genIR();
                instr = new JumpInstr(IRBuilder.getInstance().genVarName(), forStmt2);
            } else if(children.get(i) instanceof CondExp) {
                IRBuilder.getInstance().setCurBasicBlock(condBlock);
                ((CondExp) children.get(i)).genIRForCond(loopBlock, followBlock);
            }
        }
        if(forStmt1.isEmpty()) {
            IRBuilder.getInstance().setCurBasicBlock(forStmt1);
            instr = new JumpInstr(IRBuilder.getInstance().genVarName(), condBlock);
        }
        if(forStmt2.isEmpty()) {
            IRBuilder.getInstance().setCurBasicBlock(forStmt2);
            instr = new JumpInstr(IRBuilder.getInstance().genVarName(), condBlock);
        }
        if(condBlock.isEmpty()) {
            IRBuilder.getInstance().setCurBasicBlock(condBlock);
            instr = new JumpInstr(IRBuilder.getInstance().genVarName(), loopBlock);
        }
        if(loopBlock.isEmpty()) {
            IRBuilder.getInstance().setCurBasicBlock(loopBlock);
            instr = new JumpInstr(IRBuilder.getInstance().genVarName(), forStmt2);
        }
        //推出循环
        IRBuilder.getInstance().popLoop();
        IRBuilder.getInstance().setCurBasicBlock(followBlock);
        SymbolManager.getManager().leaveLoop();
        return null;
    }
}
