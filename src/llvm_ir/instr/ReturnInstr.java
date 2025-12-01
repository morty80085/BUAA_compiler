package llvm_ir.instr;

import backend.Instr.LiInstr;
import backend.Instr.MipsLoadInstr;
import backend.Instr.MipsJumpInstr;
import backend.Instr.MoveInstr;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.Constant;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class ReturnInstr extends Instr {
    public ReturnInstr(String name, Value retValue) {
        super(BaseType.VOID, name, InstrType.RETURN);
        //非void
        if(retValue != null) {
            addOperands(retValue);
        }
    }

    public Value getRetValue() {
        if(operands.isEmpty()) {
            return null;
        } else {
            return operands.get(0);
        }
    }

    @Override
    public String toString() {
        Value retValue = getRetValue();
        if(retValue == null) {
            return "ret void";
        }
        return "ret " + retValue.getType() + " " + retValue.getName();
    }

    @Override
    public void genMips() {
        Value retValue = getRetValue();
        if(retValue == null) {
            LiInstr liInstr = new LiInstr(Register.v0, 0);
            MipsBuilder.getInstance().addText(liInstr);
        } else if(retValue instanceof Constant) {
            int num = ((Constant) retValue).getValue();
            LiInstr liInstr = new LiInstr(Register.v0, num);
            MipsBuilder.getInstance().addText(liInstr);
        } else if(MipsBuilder.getInstance().getRegisterOfValue(retValue) != null) {
            Register register = MipsBuilder.getInstance().getRegisterOfValue(retValue);
            MoveInstr moveInstr = new MoveInstr(Register.v0, register);
            MipsBuilder.getInstance().addText(moveInstr);
        } else {
            int offset = MipsBuilder.getInstance().getOffsetOfValue(retValue);
            MipsLoadInstr loadInstr = new MipsLoadInstr(Register.v0, Register.sp, offset);
            MipsBuilder.getInstance().addText(loadInstr);
        }
        //jr ra
        MipsJumpInstr mipsJumpInstr = new MipsJumpInstr(MipsJumpInstr.Op.jr, Register.ra);
        MipsBuilder.getInstance().addText(mipsJumpInstr);
    }
}
