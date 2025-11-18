package llvm_ir;

import llvm_ir.type.LLvmType;

import java.util.ArrayList;

public class Initial {
    private LLvmType lLvmType;
    private ArrayList<Integer> values;

    public Initial(LLvmType lLvmType, ArrayList<Integer> values) {
        this.lLvmType = lLvmType;
        this.values = values;
    }

    public LLvmType getType()  {
        return this.lLvmType;
    }

    public ArrayList<Integer> getValues() {
        return this.values;
    }

    public boolean isUndefined() {
        return values == null;
    }

    @Override
    public String toString() {
        if(values != null) {
            if(lLvmType.isInt32()) {
                return lLvmType + " " + values.get(0);
            } else {
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < values.size(); i++) {
                    sb.append("i32 ").append(values.get(i));
                    if(i < values.size() - 1) {
                        sb.append(", ");
                    }
                }
                return lLvmType + " [" + sb.toString() + "] ";
            }
        } else {
            if(lLvmType.isInt32()) {
                return lLvmType + " 0";
            } else {
                return lLvmType + " zeroinitializer";
            }
        }
    }
}
