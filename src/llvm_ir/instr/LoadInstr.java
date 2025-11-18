package llvm_ir.instr;

import llvm_ir.Instr;
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

}
