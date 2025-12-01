package llvm_ir.instr;

import backend.Instr.*;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.Constant;
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

    @Override
    public void genMips() {
        Value operand1 = getOperand1();
        Value operand2 = getOperand2();

        Register register1 = Register.t0;
        Register register2 = Register.t1;
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
            case add:
                RRInstr addInstr = new RRInstr(RRInstr.Op.add, register3, register1, register2);
                MipsBuilder.getInstance().addText(addInstr);
                break;
            case sub:
                RRInstr subInstr = new RRInstr(RRInstr.Op.sub, register3, register1, register2);
                MipsBuilder.getInstance().addText(subInstr);
                break;
            case mul:
                RRInstr mulInstr = new RRInstr(RRInstr.Op.mult, register1, register2);
                HiLoInstr hiLoInstr = new HiLoInstr(register3, HiLoInstr.Op.mflo);
                MipsBuilder.getInstance().addText(mulInstr);
                MipsBuilder.getInstance().addText(hiLoInstr);
                break;
            case sdiv:
                RRInstr divInstr1 = new RRInstr(RRInstr.Op.div, register1, register2);
                HiLoInstr hiLoInstr1 = new HiLoInstr(register3, HiLoInstr.Op.mflo);
                MipsBuilder.getInstance().addText(divInstr1);
                MipsBuilder.getInstance().addText(hiLoInstr1);
                break;
            case srem:
                RRInstr divInstr2 = new RRInstr(RRInstr.Op.div, register1, register2);
                HiLoInstr hiLoInstr2 = new HiLoInstr(register3, HiLoInstr.Op.mfhi);
                MipsBuilder.getInstance().addText(divInstr2);
                MipsBuilder.getInstance().addText(hiLoInstr2);
                break;
            case and:
                RRInstr andInstr = new RRInstr(RRInstr.Op.and, register3, register1, register2);
                MipsBuilder.getInstance().addText(andInstr);
                break;
            case or:
                RRInstr orInstr = new RRInstr(RRInstr.Op.or, register3, register1, register2);
                MipsBuilder.getInstance().addText(orInstr);
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
