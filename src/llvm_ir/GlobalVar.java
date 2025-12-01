package llvm_ir;

import backend.Instr.*;
import backend.MipsBuilder;
import backend.Register;
import llvm_ir.type.LLvmType;
import llvm_ir.type.PointerType;

import java.util.ArrayList;

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

    @Override
    public void genMips() {
        LLvmType lLvmType = ((PointerType) type).getTargetType();
        //全局变量，分为数组和非数组
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
            SpaceInstr spaceInstr = new SpaceInstr(name.substring(1), lLvmType.getArrayLength() * 4);
            MipsBuilder.getInstance().addDate(spaceInstr);
            if(initial.getValues() != null) {
                int offset = 0;
                for (Integer value : initial.getValues()) {
                    LiInstr liInstr = new LiInstr(Register.t0, value);
                    LaInstr laInstr = new LaInstr(Register.t1, name.substring(1));
                    MipsStoreInstr mipsStoreInstr = new MipsStoreInstr(Register.t0, Register.t1, offset);
                    MipsBuilder.getInstance().addText(liInstr);
                    MipsBuilder.getInstance().addText(laInstr);
                    MipsBuilder.getInstance().addText(mipsStoreInstr);
                    offset += 4;
                }
            }
        }
    }
}
