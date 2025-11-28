package llvm_ir;

import backend.Instr.WordInstr;
import backend.MipsBuilder;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

import java.util.ArrayList;

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

    @Override
    public void genMips() {
        LLvmType lLvmType = ((PointerType) type).getTargetType();
        //静态变量，分为数组和非数组
        if(lLvmType.isInt32()) {
            if(initial.getValues() == null) {
                ArrayList<Integer> num = new ArrayList<>();
                num.add(0);
                WordInstr wordInstr = new WordInstr(name.substring(1),num);
                MipsBuilder.getInstance().addDate(wordInstr);
            } else {
                ArrayList<Integer> num = new ArrayList<>();
                int number = initial.getValues().get(0);
                num.add(number);
                WordInstr wordInstr = new WordInstr(name.substring(1),num);
                MipsBuilder.getInstance().addDate(wordInstr);
            }
        } else {
            ArrayList<Integer> num = new ArrayList<>();
            for(Integer number : initial.getValues()) {
                num.add(number);
            }
            WordInstr wordInstr = new WordInstr(name.substring(1),num);
            MipsBuilder.getInstance().addDate(wordInstr);
        }
    }
}
