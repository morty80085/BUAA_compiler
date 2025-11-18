package llvm_ir;

import llvm_ir.type.LLvmType;

public class GlobalVar extends Value{
    private Initial initial;

    public GlobalVar(LLvmType lLvmType, String name, Initial initial) {
        super(lLvmType, name);
        this.initial = initial;
        if(IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addGlobalVar(this);
        }
    }

    @Override
    public String toString() {
        return name + " = dso_local global " + initial.toString() + ", align 4";
    }
}
