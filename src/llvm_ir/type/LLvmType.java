package llvm_ir.type;


public class LLvmType {
    public boolean isArrayType() {
        return this instanceof ArrayType;
    }

    public boolean isVoid() {
        return this == BaseType.VOID;
    }

    public boolean isInt1() {
        return this == BaseType.INT1;
    }

    public boolean isInt8() {
        return this == BaseType.INT8;
    }

    public boolean isInt32() {
        return this == BaseType.INT32;
    }

    public boolean isBB() {
        return this == OtherType.BB;
    }

    public boolean isFunction() {
        return this == OtherType.Function;
    }

    public boolean isModule() {
        return this == OtherType.Module;
    }

    public boolean isPointer() {
        return this instanceof PointerType;
    }

    public int getArrayLength() {
        if(this instanceof ArrayType) {
            int eleNum = ((ArrayType)this).getEleNum();
            LLvmType eleType  = ((ArrayType)this).getEleType();
            return eleNum * eleType.getArrayLength();
        } else {
            return 1;
        }
    }
}
