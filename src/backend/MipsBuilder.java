package backend;

import backend.Instr.MipsInstr;
import llvm_ir.Function;
import llvm_ir.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class MipsBuilder {
    private Function currentFunction;
    private int currentOffset;
    private HashMap<Value, Integer> offsetMap;
    //由函数决定哪些参数会被放入寄存器
    private HashMap<Value, Register> registerMap;
    private ArrayList<MipsInstr> data;
    private ArrayList<MipsInstr> text;

    private static MipsBuilder mipsBuilder = new MipsBuilder();
    public MipsBuilder getInstance() {
        return mipsBuilder;
    }

    public MipsBuilder() {
        this.currentFunction = null;
        this.currentOffset = 0;
        this.offsetMap = new HashMap<>();
        this.registerMap = new HashMap<>();
        this.data = new ArrayList<>();
        this.text = new ArrayList<>();
    }

    public void enterFunction(Function function) {
        this.currentFunction = function;
        this.currentOffset = 0;
        this.offsetMap = new HashMap<>();
        this.registerMap = function.getRegisterMap();
    }

    public void setRegisterForValue(Value value, Register register) {
        registerMap.put(value, register);
    }

    public Register getRegisterOfValue(Value value) {
        if(registerMap.containsKey(value)) {
           return registerMap.get(value);
        }
        return null;
    }

    public void subCurrentOffset(int num) {
        currentOffset = currentOffset * num;
    }

    public void addCurrentOffset(int num) {
        currentOffset = currentOffset + num;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public void putOffset(Value value, Integer offset) {
        offsetMap.put(value, offset);
    }

    public Integer getOffsetOfValue(Value value) {
        if(offsetMap.containsKey(value)) {
            offsetMap.get(value);
        }
        return null;
    }

    public void addDate(MipsInstr mipsInstr) {
        data.add(mipsInstr);
    }

    public void addText(MipsInstr mipsInstr) {
        text.add(mipsInstr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");
        for(MipsInstr mipsInstr : data) {
            sb.append(mipsInstr.toString()).append("\n");
        }
        sb.append("\n\n.text\n");
        for(MipsInstr mipsInstr : text) {
            sb.append(mipsInstr.toString()).append("\n");
        }
        sb.append("\tli $v0,10\n");
        sb.append("\tsyscall");
        return sb.toString();
    }
}
