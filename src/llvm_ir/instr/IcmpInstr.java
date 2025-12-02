package llvm_ir.instr;

import backend.Instr.*;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.Constant;
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

    @Override
    public void genMips() {
        Value operand1 = getOperand1();
        Value operand2 = getOperand2();
        Register register1 = Register.t0;
        Register register2 = Register.t1;
        //用来存放结果的寄存器
        Register register3 = Register.t2;

        if(operand1 instanceof Constant) {
            LiInstr liInstr = new LiInstr(register1, Integer.parseInt(operand1.getName()));
            MipsBuilder.getInstance().addText(liInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(operand1) != null) {
            register1 = MipsBuilder.getInstance().getRegisterOfValue(operand1);
        } else {
            //在栈上
            int offset = MipsBuilder.getInstance().getOffsetOfValue(operand1);
            MipsLoadInstr loadInstr = new MipsLoadInstr(register1, Register.sp, offset);
            MipsBuilder.getInstance().addText(loadInstr);
        }

        if(operand2 instanceof Constant) {
            LiInstr liInstr = new LiInstr(register2, Integer.parseInt(operand2.getName()));
            MipsBuilder.getInstance().addText(liInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(operand2) != null) {
            register2 = MipsBuilder.getInstance().getRegisterOfValue(operand2);
        } else {
            //在栈上
            int offset = MipsBuilder.getInstance().getOffsetOfValue(operand2);
            MipsLoadInstr loadInstr = new MipsLoadInstr(register2, Register.sp, offset);
            MipsBuilder.getInstance().addText(loadInstr);
        }

        switch (op) {
            case eq:
                RRInstr seqInstr = new RRInstr(RRInstr.Op.seq, register3, register1, register2);
                MipsBuilder.getInstance().addText(seqInstr);
                break;
            case ne:
                RRInstr sneInstr = new RRInstr(RRInstr.Op.sne,register3, register1, register2);
                MipsBuilder.getInstance().addText(sneInstr);
                break;
            case sgt:
                RRInstr sgtInstr = new RRInstr(RRInstr.Op.sgt, register3, register1, register2);
                MipsBuilder.getInstance().addText(sgtInstr);
                break;
            case sge:
                RRInstr sgeInstr = new RRInstr(RRInstr.Op.sge, register3, register1, register2);
                MipsBuilder.getInstance().addText(sgeInstr);
                break;
            case slt:
                RRInstr sltInstr = new RRInstr(RRInstr.Op.slt, register3, register1, register2);
                MipsBuilder.getInstance().addText(sltInstr);
                break;
            case sle:
                RRInstr sleInstr = new RRInstr(RRInstr.Op.sle, register3, register1, register2);
                MipsBuilder.getInstance().addText(sleInstr);
                break;
        }

        if(MipsBuilder.getInstance().getRegisterOfValue(this) != null) {
            MoveInstr moveInstr = new MoveInstr(MipsBuilder.getInstance().getRegisterOfValue(this), register3);
            MipsBuilder.getInstance().addText(moveInstr);
        } else {
            //在栈上分配空间
            MipsBuilder.getInstance().subCurrentOffset(4);
            int offset = MipsBuilder.getInstance().getCurrentOffset();
            MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(register3, Register.sp, offset);
            MipsBuilder.getInstance().addText(mipsStoreInstr);
            MipsBuilder.getInstance().putOffset(this, offset);
        }
    }
}
