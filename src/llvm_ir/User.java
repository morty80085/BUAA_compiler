package llvm_ir;

import llvm_ir.type.ArrayType;
import llvm_ir.type.LLvmType;

import java.util.ArrayList;

public class User extends Value{
    protected ArrayList<Value> operands;

    public User(LLvmType type, String name) {
        super(type, name);
        this.operands = new ArrayList<>();
    }

    public void addOperands(Value operand) {
        operands.add(operand);
        //为operand添加引用
        if(operand != null) {
            operand.addUse(this);
        }
    }

    public ArrayList<Value> getOperands() {
        return this.operands;
    }
}
