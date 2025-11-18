package llvm_ir.instr;

import llvm_ir.Function;
import llvm_ir.Instr;
import llvm_ir.Param;
import llvm_ir.Value;
import llvm_ir.type.OtherType;

import java.util.ArrayList;
import java.util.List;

public class CallInstr extends Instr {
    public CallInstr(String name, Function function, ArrayList<Value> params) {
        super(function.getRetType(), name, InstrType.CALL);
        addOperands(function);
        for(Value param : params) {
            addOperands(param);
        }
    }

    public Function getFunction() {
        return (Function) operands.get(0);
    }

    public List<Value> getParams() {
        return operands.subList(1, operands.size());
    }

    @Override
    public String toString() {
        Function function = getFunction();
        List<Value> params = getParams();
        ArrayList<String> paramsInfo = new ArrayList<>();
        for(Value param: params) {
            paramsInfo.add(param.getType() + " " + param.getName());
        }
        if(type.isVoid()) {
            return "call void " + function.getName() + "(" + String.join(", ", paramsInfo) + ")";
        } else {
            return name + " = call i32 " + function.getName() + "(" + String.join(", ", paramsInfo) + ")";
        }
    }
}
