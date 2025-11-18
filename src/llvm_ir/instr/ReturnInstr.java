package llvm_ir.instr;

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
}
