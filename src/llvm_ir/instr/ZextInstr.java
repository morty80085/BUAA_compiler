package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.type.LLvmType;

public class ZextInstr extends Instr{
    private LLvmType targetType;

    public ZextInstr(String name, Value value, LLvmType targetType) {
        super(targetType, name, InstrType.ZEXT);
        this.targetType = targetType;
        addOperands(value);
    }

    public Value getSourceOperand() {
        return operands.get(0);
    }

    @Override
    public String toString() {
        Value source = getSourceOperand();
        return name + " = zext " + source.getType() + " " + source.getName() + " to " + this.targetType;
    }

}
