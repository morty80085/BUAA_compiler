package llvm_ir.instr;

import llvm_ir.BasicBlock;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class BranchInstr extends Instr {
    public BranchInstr(String name, Value con, BasicBlock thenBlock, BasicBlock elseBlock) {
        super(BaseType.VOID, name, InstrType.BRANCH);
        operands.add(con);
        operands.add(thenBlock);
        operands.add(elseBlock);
    }

    public Value getCon() {
        return operands.get(0);
    }

    public BasicBlock getThenBlock() {
        return (BasicBlock) operands.get(1);
    }

    public BasicBlock getElseBlock() {
        return (BasicBlock) operands.get(2);
    }

    public void setThenBlock(BasicBlock basicBlock) {
        operands.set(1, basicBlock);
    }

    public void setElseBlock(BasicBlock basicBlock) {
        operands.set(2, basicBlock);
    }

    @Override
    public String toString() {
        Value con = getCon();
        BasicBlock thenBlock = getThenBlock();
        BasicBlock elseBlock = getElseBlock();
        return "br i1 " + con.getName() + ", label %" + thenBlock.getName() + ", label %" + elseBlock.getName();
    }
}
