package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.Value;
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
}
