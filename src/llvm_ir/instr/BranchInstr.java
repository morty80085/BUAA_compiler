package llvm_ir.instr;

import backend.Instr.MipsBranchInstr;
import backend.Instr.MipsJumpInstr;
import backend.Instr.MipsLoadInstr;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.BasicBlock;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

import java.lang.management.MemoryManagerMXBean;

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

    @Override
    public void genMips() {
        Value con = getCon();
        Value thenBlock = getThenBlock();
        Value elseBlock = getElseBlock();

        Register conRegister = Register.t0;

        if(MipsBuilder.getInstance().getRegisterOfValue(con) != null) {
            conRegister = MipsBuilder.getInstance().getRegisterOfValue(con);
        } else {
            int offset = MipsBuilder.getInstance().getOffsetOfValue(con);
            MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(conRegister, Register.sp, offset);
            MipsBuilder.getInstance().addText(mipsLoadInstr);
        }

        MipsBranchInstr mipsBranchInstr = new MipsBranchInstr(MipsBranchInstr.Op.bne, conRegister ,Register.zero, thenBlock.getName());
        MipsJumpInstr mipsJumpInstr = new MipsJumpInstr(MipsJumpInstr.Op.j, elseBlock.getName());
        MipsBuilder.getInstance().addText(mipsBranchInstr);
        MipsBuilder.getInstance().addText(mipsJumpInstr);
    }
}
