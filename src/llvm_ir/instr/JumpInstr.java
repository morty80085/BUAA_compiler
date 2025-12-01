package llvm_ir.instr;

import backend.Instr.MipsJumpInstr;
import backend.MipsBuilder;
import llvm_ir.BasicBlock;
import llvm_ir.Instr;
import llvm_ir.type.BaseType;

public class JumpInstr extends Instr {
    public JumpInstr(String name, BasicBlock targetBB) {
        super(BaseType.VOID, name, InstrType.JUMP);
        operands.add(targetBB);
    }

    public BasicBlock getTargetBB() {
        return (BasicBlock)operands.get(0);
    }

    @Override
    public String toString() {
        BasicBlock targetBB = getTargetBB();
        return "br label %" + targetBB.getName();
    }

    @Override
    public void genMips() {
        BasicBlock targetBB = getTargetBB();
        String name = targetBB.getName();
        MipsJumpInstr mipsJumpInstr = new MipsJumpInstr(MipsJumpInstr.Op.j, name);
        MipsBuilder.getInstance().addText(mipsJumpInstr);
    }
}
