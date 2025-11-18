package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.Value;
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
}
