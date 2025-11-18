package llvm_ir.type;

public class PointerType extends LLvmType{
    private LLvmType targetType;

    public PointerType(LLvmType targetType) {
        this.targetType = targetType;
    }

    public LLvmType getTargetType() {
        return this.targetType;
    }

    @Override
    public String toString() {
        return targetType.toString() + "*";
    }
}
