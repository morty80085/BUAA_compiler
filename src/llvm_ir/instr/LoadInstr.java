package llvm_ir.instr;

import backend.Instr.*;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.GlobalVar;
import llvm_ir.Instr;
import llvm_ir.StaticVar;
import llvm_ir.Value;
import llvm_ir.type.PointerType;

public class LoadInstr extends Instr {
    public LoadInstr(String name, Value pointer) {
        super(((PointerType)pointer.getType()).getTargetType(), name, InstrType.LOAD);
        //pointer是指针类型，代表一个地址
        operands.add(pointer);
    }

    public Value getPointer() {
        return operands.get(0);
    }

    @Override
    public String toString() {
        Value pointer = getPointer();
        return name + " = load " + type + ", " + pointer.getType() + " " + pointer.getName();
    }

    //	%v163 = load i32, i32* %v59
    @Override
    public void genMips() {
        Register base = Register.t0;
        Register rt = Register.t1;
        Value pointer = getPointer();

        //先计算出地址
        if(pointer instanceof GlobalVar) {
            LaInstr laInstr = new LaInstr(base, pointer.getName().substring(1));
            MipsBuilder.getInstance().addText(laInstr);
        } else if(pointer instanceof StaticVar) {
            LaInstr laInstr = new LaInstr(base, pointer.getName().substring(1));
            MipsBuilder.getInstance().addText(laInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(pointer) != null) {
            base = MipsBuilder.getInstance().getRegisterOfValue(pointer);
        } else {
            int offset = MipsBuilder.getInstance().getOffsetOfValue(pointer);
            MipsLoadInstr loadInstr = new MipsLoadInstr(base, Register.sp, offset);
            MipsBuilder.getInstance().addText(loadInstr);
        }
        //取值
        MipsLoadInstr mipsLoadInstr = new MipsLoadInstr(rt, base, 0);
        MipsBuilder.getInstance().addText(mipsLoadInstr);
        //将取出的值放到对应的地方
        if(MipsBuilder.getInstance().getRegisterOfValue(this) != null) {
            Register register = MipsBuilder.getInstance().getRegisterOfValue(this);
            MoveInstr moveInstr = new MoveInstr(register, rt);
            MipsBuilder.getInstance().addText(moveInstr);
        } else {
            //将值存放到栈上
            MipsBuilder.getInstance().subCurrentOffset(4);
            int offset = MipsBuilder.getInstance().getCurrentOffset();
            MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(rt,Register.sp,offset);
            MipsBuilder.getInstance().addText(mipsStoreInstr);
            MipsBuilder.getInstance().putOffset(this, offset);
        }
    }

}
