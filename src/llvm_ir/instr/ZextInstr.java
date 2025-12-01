package llvm_ir.instr;

import backend.Instr.MipsStoreInstr;
import backend.MipsBuilder;
import backend.Register;
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

    @Override
    public void genMips() {
        Value value = getSourceOperand();
        if(MipsBuilder.getInstance().getRegisterOfValue(value) != null) {
            //如果分配了寄存器，要重新加载到栈上
            Register rt = MipsBuilder.getInstance().getRegisterOfValue(value);
            MipsBuilder.getInstance().subCurrentOffset(4);
            int offset = MipsBuilder.getInstance().getCurrentOffset();
            MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(rt,Register.sp,offset);
            MipsBuilder.getInstance().addText(mipsStoreInstr);
            MipsBuilder.getInstance().putOffset(this,offset);
        } else {
            //如果存储在栈上,直接调用对应的地址
            MipsBuilder.getInstance().putOffset(this, MipsBuilder.getInstance().getOffsetOfValue(value));
        }
    }
}
