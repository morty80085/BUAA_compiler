package llvm_ir;

import backend.Instr.LabelInstr;
import backend.MipsBuilder;
import llvm_ir.type.OtherType;

import java.util.LinkedList;

public class BasicBlock extends Value{
    private LinkedList<Instr> instrList;
    private Function parentFunction;

    public BasicBlock(String name) {
        super(OtherType.BB, name);
        this.instrList = new LinkedList<>();
        this.parentFunction = null;
        if(IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addBB(this);
        }
    }

    public void addInstr(Instr instr) {
        instrList.add(instr);
    }

    public void setParentFunction(Function parentFunction) {
        this.parentFunction = parentFunction;
    }

    public Function getParentFunction() {
        return this.parentFunction;
    }

    public LinkedList<Instr> getInstrList() {
        return this.instrList;
    }

    public boolean isEmpty() {
        return instrList.isEmpty();
    }

    public Instr getFirst() {
        return instrList.getFirst();
    }

    public Instr getLast() {
        return instrList.getLast();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n\t");

        for(int i = 0; i < instrList.size(); i++) {
            Instr instr = instrList.get(i);
            sb.append(instr.toString());
            if(i < instrList.size() - 1) {
                sb.append("\n\t");
            }
        }

        return sb.toString();
    }

    @Override
    public void genMips() {
        LabelInstr labelInstr = new LabelInstr(name);
        MipsBuilder.getInstance().addText(labelInstr);
        for(Instr instr :instrList) {
            instr.genMips();
        }
    }
}
