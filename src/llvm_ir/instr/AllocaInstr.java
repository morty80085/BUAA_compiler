package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

public class AllocaInstr extends Instr{
    private LLvmType lLvmType;

    public AllocaInstr(String name, LLvmType lLvmType) {
        super(new PointerType(lLvmType), name, InstrType.ALLOCA);
        this.lLvmType = lLvmType;
    }

    @Override
    public String toString() {
        return name + " = alloca " + lLvmType;
    }
}
