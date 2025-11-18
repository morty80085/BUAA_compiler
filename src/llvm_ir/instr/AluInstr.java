package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class AluInstr extends Instr {
    public enum Op{
        add,
        sub,
        mul,
        sdiv,
        srem,
        and,
        or
    }

    private Op op;

    public AluInstr(String name, Op op, Value operand1, Value operand2) {
        super(BaseType.INT32, name, InstrType.ALU);
        operands.add(operand1);
        operands.add(operand2);
        this.op = op;
    }

    public Value getOperand1() {
        return operands.get(0);
    }

    public Value getOperand2() {
        return operands.get(1);
    }

    @Override
    public String toString() {
        Value operand1 = getOperand1();
        Value operand2 = getOperand2();
        return name +  " = " + op + " i32 " + operand1.getName() + ", " + operand2.getName();
    }
}
