package llvm_ir;

import llvm_ir.type.LLvmType;

public class StaticVar extends Value{
    private Initial initial;

    public StaticVar(LLvmType lLvmType, String name, Initial initial) {
        super(lLvmType, name);
        this.initial = initial;
        if(IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addStaticVar(this);
        }
    }

    @Override
    public String toString() {
        return name + " = internal global " + initial.toString() + ", align 4";
    }
}
