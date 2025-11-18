package llvm_ir;

import java.util.HashMap;
import java.util.Stack;

public class IRBuilder {
    //表示未开始优化，直接插入代码。
    public static final int AUTO_INSERT_MODE = 1;
    public static final int DEFAULT_MODE = 0;
    public static int mode = AUTO_INSERT_MODE;

    private static IRBuilder irBuilder = new IRBuilder();

    //单例模式
    public static IRBuilder getInstance() {
        return irBuilder;
    }
    //字符前缀作为名称开头
    private static final String GLOBAL_VAR_PREFIX = "@g";
    private static final String STATIC_VAR_PREFIX = "@static";
    private static final String STRING_LITERAL_PREFIX = "@string";
    private static final String VAR_NAME_PREFIX = "%v";
    private static final String PARAM_NAME_PREFIX = "%a";
    private static final String BB_NAME_PREFIX = "b";
    private static final String FUNC_NAME_PREFIX = "@f_";

    private int globalCnt;
    private int staticCnt;
    private int stringLiteralCnt;
    private HashMap<Function, Integer> varMap;
    private int paramCnt;
    private int bbCnt;

    private Module curModule;
    private Function curFunction;
    private BasicBlock curBasicBlock;

    private Stack<Loop> loopStack;

    public IRBuilder() {
        this.globalCnt = 0;
        this.staticCnt = 0;
        this.stringLiteralCnt = 0;
        this.varMap = new HashMap<>();
        this.paramCnt = 0;
        this.bbCnt = 0;
        this.curModule = new Module();
        this.curFunction = null;
        this.curBasicBlock = null;
        this.loopStack = new Stack<>();
    }

    public void addGlobalVar(GlobalVar globalVar) {
        curModule.addGlobal(globalVar);
    }

    public void addStaticVar(StaticVar staticVar) {
        curModule.addStaticVar(staticVar);
    }

    public void addStringLiteral(StringLiteral stringLiteral) {
        curModule.addStringLiteral(stringLiteral);
    }

    public void addFunction(Function function) {
        curModule.addFunction(function);
    }

    public void addBB(BasicBlock basicBlock) {
        curFunction.addBB(basicBlock);
        basicBlock.setParentFunction(curFunction);
    }

    public void addParam(Param param) {
        curFunction.addParam(param);
        param.setParentFunction(curFunction);
    }

    public void addInstr(Instr instr) {
        curBasicBlock.addInstr(instr);
        instr.setParentBB(curBasicBlock);
    }

    //生成名字
    public String genGlobalVarName() {
        return GLOBAL_VAR_PREFIX + globalCnt++;
    }

    public String genStaticVarName() {
        return STATIC_VAR_PREFIX + staticCnt++;
    }

    public String genStringLiteralName() {
        return STRING_LITERAL_PREFIX + stringLiteralCnt++;
    }

    public String genVarName() {
        int index = varMap.get(curFunction);
        String name = VAR_NAME_PREFIX + index;
        varMap.put(curFunction, index + 1);
        return name;
    }

    public String genVarName(Function function) {
        int index = varMap.get(function);
        String name = VAR_NAME_PREFIX + index;
        varMap.put(function, index + 1);
        return name;
    }

    public String genParamName() {
        return PARAM_NAME_PREFIX + paramCnt++;
    }

    public String genBBName() {
        return BB_NAME_PREFIX + bbCnt++;
    }

    public String genFunctionName(String name) {
        if(name.equals("getint")) {
            return "@" + name;
        }
        return FUNC_NAME_PREFIX + name;
    }

    //与控制有关
    public void pushLoop(Loop loop) {
        loopStack.push(loop);
    }

    public void popLoop(Loop loop) {
        loopStack.pop();
    }

    public Loop getCurLoop() {
        return loopStack.peek();
    }

    public void setCurFunction(Function function) {
        this.varMap.put(function, 0);
        this.curFunction = function;
    }

    public void setCurBasicBlock(BasicBlock basicBlock) {
        this.curBasicBlock = basicBlock;
    }

    public Function getCurFunction() {
        return this.curFunction;
    }

    public BasicBlock getCurBasicBlock() {
        return this.curBasicBlock;
    }

    public Module getCurModule() {
        return this.curModule;
    }

}
