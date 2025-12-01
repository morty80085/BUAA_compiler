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
    public static MipsBuilder getInstance() {
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

    public ArrayList<Register> getAllocRegister() {
        return new ArrayList<>(registerMap.values());
    }

    public void subCurrentOffset(int num) {
        currentOffset = currentOffset - num;
    }

    public void addCurrentOffset(int num) {
        currentOffset = currentOffset + num;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public void putOffset(Value value, int offset) {
        offsetMap.put(value, offset);
    }

    public Integer getOffsetOfValue(Value value) {
        if(offsetMap.containsKey(value)) {
            return offsetMap.get(value);
        }
        return null;
    }

    public String printOffsetMap() {
        StringBuilder sb = new StringBuilder();
        for(Value value: offsetMap.keySet()) {
            sb.append(value).append(" ").append(offsetMap.get(value)).append("\n");
        }
        return sb.toString();
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
            sb.append(mipsInstr.toString());
        }
        sb.append("\n\n.text\n");
        for(MipsInstr mipsInstr : text) {
            sb.append(mipsInstr.toString());
        }
        sb.append("end:\n");
        sb.append("\tli $v0,10\n");
        sb.append("\tsyscall");
        return sb.toString();
    }
}
