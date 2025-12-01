package llvm_ir.instr;

import backend.Instr.MipsStoreInstr;
import backend.Instr.RIInstr;
import backend.MipsBuilder;
import backend.Register;
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

    @Override
    public void genMips() {
        //先分配出一片空间来存储
        if(lLvmType.isArrayType()) {
            MipsBuilder.getInstance().subCurrentOffset(lLvmType.getArrayLength() * 4);
        } else {
            MipsBuilder.getInstance().subCurrentOffset(4);
        }
        //之后保存这个地址
        if(MipsBuilder.getInstance().getRegisterOfValue(this) != null) {
            Register register = MipsBuilder.getInstance().getRegisterOfValue(this);
            int offset = MipsBuilder.getInstance().getCurrentOffset();
            RIInstr riInstr = new RIInstr(RIInstr.Op.addi, register, Register.sp, offset);
            MipsBuilder.getInstance().addText(riInstr);
        } else {
            //要将这个地址放在栈上
            int offset = MipsBuilder.getInstance().getCurrentOffset();
            //addi t0, sp, offset（计算地址）
            RIInstr riInstr = new RIInstr(RIInstr.Op.addi, Register.t0, Register.sp, offset);
            MipsBuilder.getInstance().addText(riInstr);
            //将地址的值放入栈上
            MipsBuilder.getInstance().subCurrentOffset(4);
            offset = MipsBuilder.getInstance().getCurrentOffset();
            MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(Register.t0,Register.sp,offset);
            MipsBuilder.getInstance().addText(mipsStoreInstr);
            MipsBuilder.getInstance().putOffset(this,offset);
        }
    }
}
