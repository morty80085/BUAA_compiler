package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class IcmpInstr extends Instr {
    public enum Op{
        eq,     //等于
        ne,     //不等于
        sgt,    //大于
        sge,    //大于等于
        slt,    //小于
        sle     //小于等于
    }

    private Op op;

    public IcmpInstr(String name, Op op, Value operand1, Value operand2) {
        super(BaseType.INT1, name, InstrType.ICMP);
        this.op = op;
        operands.add(operand1);
        operands.add(operand2);
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
        return name + " = icmp " + op + " i32 " + operand1.getName() + ", " + operand2.getName();
    }
}
