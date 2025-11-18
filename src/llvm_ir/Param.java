package llvm_ir;

import llvm_ir.type.LLvmType;

public class Param extends Value{
    private Function parentFunction;

    public Param(LLvmType type, String name) {
        super(type, name);
        if(IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addParam(this);
        }
    }

    public void setParentFunction(Function parentFunction) {
        this.parentFunction = parentFunction;
    }

    public Function getParentFunction() {
        return this.parentFunction;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
