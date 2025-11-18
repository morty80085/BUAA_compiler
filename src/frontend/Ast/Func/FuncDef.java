package frontend.Ast.Func;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Stmt.ReturnStmt;
import frontend.Block;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolManager;
import frontend.symbol.SymbolType;
import frontend.symbol.ValueType;
import llvm_ir.BasicBlock;
import llvm_ir.IRBuilder;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLvmType;
import llvm_ir.Function;

import java.util.ArrayList;

public class FuncDef extends Node{
    private FuncSymbol symbol;

    public FuncDef(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
    }

    public FuncSymbol createSymbol() {
        String symbolName = ((TokenNode) children.get(1)).getToken().getTokenContent();
        SymbolType symbolType = null;
        if(((TokenNode) children.get(0).getChildren().get(0)).getToken().getTokenContent().equals("int")) {
            symbolType = SymbolType.IntFunc;
        } else {
            symbolType = SymbolType.VoidFunc;
        }
        ValueType returnType = ((FuncType)children.get(0)).getFuncType();
        return new FuncSymbol(symbolName, symbolType, returnType);
    }

    public void setFParamsInfo() {
        ArrayList<ValueType> FParamsType = new ArrayList<>();
        ArrayList<Integer> FParamsDims = new ArrayList<>();
        if(children.get(3) instanceof FuncFParams) {
            FuncFParams funcFParams = ((FuncFParams) children.get(3));
            FParamsType = funcFParams.getValueTypes();
            FParamsDims = funcFParams.getFuncFDimes();
        }
        symbol.setFuncInfo(FParamsType, FParamsDims);
    }

    @Override
    public void visit() {
        SymbolManager.getManager().setGlobal(false);
        this.symbol = createSymbol();
        //Error b
        boolean abs = SymbolManager.getManager().addSymbol(symbol);
        if(!abs) {
            ErrorRecorder.addError(new Error(Error.ErrorType.b, children.get(1).getStartLine()));
        }

        SymbolManager.getManager().enterFunc(symbol);

        for(Node child : children) {
            if(child instanceof Block) {
                //此时形参已经被解析完毕，可以调用
                setFParamsInfo();
            }
            child.visit();
        }
        //离开函数定义
        SymbolManager.getManager().leaveFunc();
        //Error g
        Node block = children.get(children.size() - 1);
        Node last = block.getChildren().get(block.getChildren().size() - 2);
        if(symbol.getReturnType() == ValueType.INT && !(last instanceof ReturnStmt)) {
            ErrorRecorder.addError(new Error(Error.ErrorType.g, endLine));
        }
    }

    @Override
    public Value genIR() {
        SymbolManager.getManager().setGlobal(false);
        SymbolManager.getManager().addSymbol(symbol);
        SymbolManager.getManager().enterFunc(symbol);
        //先生成IR
        String name = IRBuilder.getInstance().genFunctionName(symbol.getSymbolName());
        LLvmType lLvmType = null;
        if(symbol.getReturnType() == ValueType.INT) {
            lLvmType = BaseType.INT32;
        } else {
            lLvmType = BaseType.VOID;
        }
        //生成function插入到module中
        Function function = new Function(name, lLvmType);
        symbol.setlLvmValue(function);
        IRBuilder.getInstance().setCurFunction(function);
        //生成一个新的块，用来存放函数体
        String BBName = IRBuilder.getInstance().genBBName();
        BasicBlock basicBlock = new BasicBlock(BBName);
        IRBuilder.getInstance().setCurBasicBlock(basicBlock);
        //子递归解析
        super.genIR();
        //如果是void型函数，生成返回语句
        function.checkRet();
        SymbolManager.getManager().leaveFunc();
        return null;
    }
}

