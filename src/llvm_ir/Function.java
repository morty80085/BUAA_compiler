package llvm_ir;

import llvm_ir.instr.ReturnInstr;
import llvm_ir.type.LLvmType;
import llvm_ir.type.OtherType;

import java.util.ArrayList;
import java.util.LinkedList;

public class Function extends User{
    //形参列表
    private ArrayList<Param> paramList;
    //Function下的BasicBlock
    private LinkedList<BasicBlock> BBList;
    //返回类型
    private LLvmType retType;

    public Function(String name, LLvmType retType) {
        super(OtherType.Function, name);
        this.paramList = new ArrayList<>();
        this.BBList = new LinkedList<>();
        this.retType = retType;
        if(IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addFunction(this);
        }
    }

    public void addParam(Param param) {
        paramList.add(param);
    }

    public void addBB(BasicBlock basicBlock) {
        BBList.add(basicBlock);
    }

    public ArrayList<Param> getParamList() {
        return this.paramList;
    }

    public LinkedList<BasicBlock> getBBList() {
        return this.BBList;
    }

    public LLvmType getRetType() {
        return this.retType;
    }

    public void checkRet() {
        //一定要有一句返回语句
        BasicBlock lastBlock = IRBuilder.getInstance().getCurBasicBlock();
        if(lastBlock.isEmpty() || !(lastBlock.getLast() instanceof ReturnInstr)) {
            if(retType.isInt32()) {
                //默认的返回0
                new ReturnInstr(IRBuilder.getInstance().genVarName(), new Constant(0));
            } else {
                new ReturnInstr(IRBuilder.getInstance().genVarName(), null);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder paramBuilder = new StringBuilder();
        for(int i = 0; i < paramList.size(); i++) {
            paramBuilder.append(paramList.get(i).toString());
            if(i < paramList.size() - 1) {
                paramBuilder.append(", ");
            }
        }
        String paramInfo = paramBuilder.toString();

        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ").append(retType.toString()).append(" ").append(name).append("(").append(paramInfo).append(") {\n");

        for (int i = 0; i < BBList.size(); i++) {
            sb.append(BBList.get(i).toString());
            if(i < BBList.size()) {
                sb.append("\n");
            }
        }

        sb.append("\n}");
        return sb.toString();
    }
}
