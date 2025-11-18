package llvm_ir.type;

public class ArrayType extends LLvmType{
    private int eleNum;
    private LLvmType eleType;

    public ArrayType(int eleNum, LLvmType eleType) {
        this.eleNum = eleNum;
        this.eleType = eleType;
    }

    public int getEleNum() {
        return this.eleNum;
    }

    public LLvmType getEleType() {
        return this.eleType;
    }

    @Override
    public String toString() {
        return "[" + eleNum + " x " + eleType + "]";
    }
}
