package llvm_ir;

import llvm_ir.instr.IOInstr;
import llvm_ir.type.OtherType;

import java.util.ArrayList;
import java.util.LinkedList;

public class Module extends Value{
    private ArrayList<String> declareList;
    private LinkedList<StringLiteral> stringLiterals;
    private LinkedList<GlobalVar> globalVars;
    private LinkedList<StaticVar> staticVars;
    private LinkedList<Function> functions;

    public Module() {
        super(OtherType.Module, "module");
        this.declareList = new ArrayList<>();
        this.stringLiterals = new LinkedList<>();
        this.globalVars = new LinkedList<>();
        this.staticVars = new LinkedList<>();
        this.functions = new LinkedList<>();
        //添加IO指令定义
        this.declareList.add(IOInstr.GetInt.getDeclare());
        this.declareList.add(IOInstr.PutInt.getDeclare());
        this.declareList.add(IOInstr.PutStr.getDeclare());
    }

    public void addStringLiteral(StringLiteral stringLiteral) {
        stringLiterals.add(stringLiteral);
    }

    public void addGlobal(GlobalVar globalVar) {
        globalVars.add(globalVar);
    }

    public void addStaticVar(StaticVar staticVar) {
        staticVars.add(staticVar);
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    public LinkedList<StringLiteral> getStringLiterals() {
        return this.stringLiterals;
    }

    public LinkedList<GlobalVar> getGlobalVars() {
        return this.globalVars;
    }

    public LinkedList<StaticVar> getStaticVars() {
        return this.staticVars;
    }

    public LinkedList<Function> getFunctions() {
        return this.functions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 处理 declareList
        for (int i = 0; i < declareList.size(); i++) {
            sb.append(declareList.get(i));
            if (i < declareList.size() - 1) {
                sb.append("\n");
            }
        }
        sb.append("\n\n");

        // 处理 stringLiterals
        for (int i = 0; i < stringLiterals.size(); i++) {
            sb.append(stringLiterals.get(i).toString());
            if (i < stringLiterals.size() - 1) {
                sb.append("\n");
            }
        }
        sb.append("\n\n");

        // 处理 globalVarList
        for (int i = 0; i < globalVars.size(); i++) {
            sb.append(globalVars.get(i).toString());
            if (i < globalVars.size() - 1) {
                sb.append("\n");
            }
        }
        sb.append("\n\n");

        // 处理 staticVarList
        for (int i = 0; i < staticVars.size(); i++) {
            sb.append(staticVars.get(i).toString());
            if (i < staticVars.size() - 1) {
                sb.append("\n");
            }
        }
        sb.append("\n\n");

        // 处理 functionList
        for (int i = 0; i < functions.size(); i++) {
            sb.append(functions.get(i).toString());
            if (i < functions.size() - 1) {
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }
}
