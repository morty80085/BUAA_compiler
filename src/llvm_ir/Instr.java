package llvm_ir;

import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;

public class Instr extends User{
    public static enum InstrType {
        ALU,
        ALLOCA,
        BRANCH,
        CALL,
        GEP,    //数组
        ICMP,
        JUMP,
        LOAD,
        RETURN,
        STORE,
        ZEXT,
        IO
    }

    private InstrType instrType;
    //表示该语句属于哪一块
    private BasicBlock parentBB;

    public Instr(LLvmType type, String name, InstrType instrType) {
        super(type, name);
        this.instrType = instrType;
        this.parentBB = null;
        if (IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addInstr(this);
        }
    }

    public void setParentBB(BasicBlock parentBB) {
        this.parentBB = parentBB;
    }

    public BasicBlock getParentBB() {
        return this.parentBB;
    }

    public InstrType getInstrType() {
        return this.instrType;
    }
}
