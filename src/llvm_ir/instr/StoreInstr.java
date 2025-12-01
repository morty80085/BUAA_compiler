package llvm_ir.instr;

import backend.Instr.LaInstr;
import backend.Instr.LiInstr;
import backend.Instr.MipsLoadInstr;
import backend.Instr.MipsStoreInstr;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.*;
import llvm_ir.type.BaseType;

public class StoreInstr extends Instr {
    public StoreInstr(String name, Value from, Value to) {
        super(BaseType.VOID, name, InstrType.STORE);
        operands.add(from);
        operands.add(to);
    }

    public Value getFrom() {
        return operands.get(0);
    }

    public Value getTo() {
        return operands.get(1);
    }

    @Override
    public String toString() {
        Value from = getFrom();
        Value to = getTo();
        return "store " + from.getType() + " " + from.getName() + ", " + to.getType() + " " + to.getName();
    }

    //store i32 %v3, i32* @static0
    //store i32 %v11, i32* %v2
    @Override
    public void genMips() {
        //使用sw语句将值存入目标的地址
        //值存放的寄存器
        Register rt = Register.t0;
        //目标地址的寄存器
        Register base = Register.t1;
        //值
        Value from = getFrom();
        //地址
        Value to = getTo();

        if(from instanceof Constant) {
            LiInstr liInstr = new LiInstr(rt, Integer.parseInt(from.getName()));
            MipsBuilder.getInstance().addText(liInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(from) != null) {
            rt = MipsBuilder.getInstance().getRegisterOfValue(from);
        } else {
            //在栈上
            int offset = MipsBuilder.getInstance().getOffsetOfValue(from);
            MipsLoadInstr loadInstr = new MipsLoadInstr(rt, Register.sp, offset);
            MipsBuilder.getInstance().addText(loadInstr);
        }

        if(to instanceof GlobalVar) {
            LaInstr laInstr = new LaInstr(base, to.getName().substring(1));
            MipsBuilder.getInstance().addText(laInstr);
        } else if(to instanceof StaticVar) {
            LaInstr laInstr = new LaInstr(base, to.getName().substring(1));
            MipsBuilder.getInstance().addText(laInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(to) != null) {
            base = MipsBuilder.getInstance().getRegisterOfValue(to);
        } else {
            //在栈上
            int offset = MipsBuilder.getInstance().getOffsetOfValue(to);
            MipsLoadInstr loadInstr = new MipsLoadInstr(base, Register.sp, offset);
            MipsBuilder.getInstance().addText(loadInstr);
        }

        MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(rt, base, 0);
        MipsBuilder.getInstance().addText(mipsStoreInstr);
    }
}
