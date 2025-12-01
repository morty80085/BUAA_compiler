package llvm_ir.instr;

import backend.Instr.*;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.*;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

public class GepInstr extends Instr {
//   %2 = getelementptr [5 x i32], [5 x i32]* @a, i32 0, i32 0
//   %3 = getelementptr i32, i32* %2, i32 3
//   %4 = getelementptr inbounds i32, i32* %3, i32 1
     public GepInstr(String name, Value pointer, Value offset) {
         //gep语句返回的是指针类型
         super(new PointerType(BaseType.INT32), name, InstrType.GEP);
         operands.add(pointer);
         operands.add(offset);
     }

     public Value getPointer() {
         return operands.get(0);
     }

     public Value getOffset() {
         return operands.get(1);
     }

     @Override
    public String toString() {
         Value pointer = getPointer();
         Value offset = getOffset();
         PointerType pointerType = (PointerType) pointer.getType();
         LLvmType lLvmType = pointerType.getTargetType();
         if(lLvmType.isArrayType()) {
             //数组
             return name + " = getelementptr inbounds " + lLvmType + ", " + pointerType + " "
                     + pointer.getName() + ", i32 0, i32 " + offset.getName();
         } else {
            //i32
             return name + " = getelementptr inbounds " + lLvmType + ", " + pointerType + " "
                     + pointer.getName() + ", i32 " + offset.getName();
         }
     }

     //%v1 = getelementptr inbounds i32, i32* %a1, i32 0
     //%v8 = getelementptr inbounds [1 x i32], [1 x i32]* %v6, i32 0, i32 0
    @Override
    public void genMips() {
         Value pointer = getPointer();
         Value offset = getOffset();
         Register addressRegister = Register.t0;
         Register offsetRegister = Register.t1;
         Register targetRegister = Register.t3;

         //先加载地址
        if(pointer instanceof GlobalVar) {
            LaInstr laInstr = new LaInstr(addressRegister, pointer.getName().substring(1));
            MipsBuilder.getInstance().addText(laInstr);
        } else if(pointer instanceof StaticVar) {
            LaInstr laInstr = new LaInstr(addressRegister, pointer.getName().substring(1));
            MipsBuilder.getInstance().addText(laInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(pointer) != null) {
            addressRegister = MipsBuilder.getInstance().getRegisterOfValue(pointer);
        } else {
            //在栈上
            int offset1 = MipsBuilder.getInstance().getOffsetOfValue(pointer);
            MipsLoadInstr loadInstr = new MipsLoadInstr(addressRegister, Register.sp, offset1);
            MipsBuilder.getInstance().addText(loadInstr);
        }

        //再将值取到寄存器中
        if(offset instanceof Constant) {
            LiInstr liInstr = new LiInstr(offsetRegister, Integer.parseInt(offset.getName()));
            MipsBuilder.getInstance().addText(liInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(offset) != null) {
            offsetRegister = MipsBuilder.getInstance().getRegisterOfValue(offset);
        } else {
            //在栈上
            int offset1 = MipsBuilder.getInstance().getOffsetOfValue(offset);
            MipsLoadInstr loadInstr = new MipsLoadInstr(offsetRegister, Register.sp, offset1);
            MipsBuilder.getInstance().addText(loadInstr);
        }

        //offset = offset << 4
        RIInstr riInstr = new RIInstr(RIInstr.Op.sll, offsetRegister, offsetRegister, 2);
        //计算出最终的地址
        RRInstr rrInstr = new RRInstr(RRInstr.Op.add, targetRegister, offsetRegister, addressRegister);
        MipsBuilder.getInstance().addText(riInstr);
        MipsBuilder.getInstance().addText(rrInstr);

        //将对应值存放
        if(MipsBuilder.getInstance().getRegisterOfValue(this) != null) {
            MoveInstr moveInstr = new MoveInstr(MipsBuilder.getInstance().getRegisterOfValue(this), targetRegister);
            MipsBuilder.getInstance().addText(moveInstr);
        } else {
            MipsBuilder.getInstance().subCurrentOffset(4);
            int curOffset = MipsBuilder.getInstance().getCurrentOffset();
            MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(targetRegister, Register.sp, curOffset);
            MipsBuilder.getInstance().addText(mipsStoreInstr);
            MipsBuilder.getInstance().putOffset(this, curOffset);
        }
    }
}
