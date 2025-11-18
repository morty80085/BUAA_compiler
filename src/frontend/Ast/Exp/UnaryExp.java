package frontend.Ast.Exp;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import error.Error;
import error.ErrorRecorder;
import frontend.*;
import frontend.Ast.Func.FuncRParams;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolManager;
import frontend.symbol.ValueType;
import llvm_ir.*;
import llvm_ir.instr.*;
import llvm_ir.type.BaseType;

import java.util.ArrayList;

//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
public class UnaryExp extends Node{
    public UnaryExp(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    @Override
    public Integer getDim() {
        if(children.get(0) instanceof TokenNode) {
            String funcName = ((TokenNode) children.get(0)).getToken().getTokenContent();
            FuncSymbol funcSymbol = (FuncSymbol) SymbolManager.getManager().getSymbolByName(funcName);
            if(funcSymbol.getReturnType() == ValueType.INT) {
                return 0;
            } else {
                return null;
            }
        }
        for(Node child : children) {
            if(child.getDim() != null) {
                return child.getDim();
            }
        }
        return null;
    }

    @Override
    public int execute() {
        //此方法目的是算出primaryExp的值和UnaryOp UnaryExp, 函数变量直接赋值为零（因为不会使用，此方法只有计算全局变量，const变量和static变量时会使用）
        int ans = 0;
        if(children.get(0) instanceof UnaryOp) {
            //UnaryOp UnaryExp
            TokenNode tokenNode = (TokenNode) children.get(0).getChildren().get(0);
            UnaryExp unaryExp = (UnaryExp) children.get(1);
            if(tokenNode.getToken().getTokenType() == TokenType.PLUS) {
                //+
                ans = unaryExp.execute();
            } else if (tokenNode.getToken().getTokenType() == TokenType.MINU){
                //-
                ans = -1 * unaryExp.execute();
            } else {
                //!
                if(unaryExp.execute() == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else if (children.get(0) instanceof PrimaryExp) {
            ans = children.get(0).execute();
        }
        return ans;
    }

    @Override
    public void visit() {
        //先检查是否是函数
        if(children.get(0) instanceof TokenNode) {
            String funcName = ((TokenNode) children.get(0)).getToken().getTokenContent();
            FuncSymbol funcSymbol = (FuncSymbol) SymbolManager.getManager().getSymbolByName(funcName);
            //Error c
            if(funcSymbol == null) {
                ErrorRecorder.addError(new Error(Error.ErrorType.c,children.get(0).getStartLine()));
                super.visit();
                return;
            }
            ArrayList<Integer> funcFParams = funcSymbol.getFuncFDims();
            ArrayList<Integer> funcRParams = new ArrayList<>();
            if(children.size() >2 && children.get(2) instanceof FuncRParams) {
                funcRParams = ((FuncRParams) children.get(2)).getFuncRParams();
            }
            //Error d
            if(funcFParams.size() != funcRParams.size()) {
                ErrorRecorder.addError(new Error(Error.ErrorType.d,children.get(2).getEndLine()));
                super.visit();;
                return;
            }
            //Error e
            for(int i = 0; i < funcFParams.size(); i++) {
                if(! funcFParams.get(i).equals(funcRParams.get(i))) {
                    ErrorRecorder.addError(new Error(Error.ErrorType.e,children.get(2).getEndLine()));
                    super.visit();
                    return;
                }
            }

        }
        //否则直接使用Node的visit
        super.visit();
    }

    @Override
    public Value genIR() {
        //primaryExp
        if(children.get(0) instanceof PrimaryExp) {
            return children.get(0).genIR();
        } else if(children.get(0) instanceof UnaryOp) {
            TokenNode tokenNode = (TokenNode) children.get(0).getChildren().get(0);
            Instr instr = null;
            Value operand1 = children.get(1).genIR();
            //用于处理-和！的情况
            Value operand2 = new Constant(0);
            if(tokenNode.getToken().getTokenType() == TokenType.PLUS) {
                //返回的是一个Value，方便之前的调用使用
                return operand1;
            } else if(tokenNode.getToken().getTokenType() == TokenType.MINU) {
                //-value = 0 - value
                instr = new AluInstr(IRBuilder.getInstance().genVarName(), AluInstr.Op.sub, operand2, operand1);
                return instr;
            } else {
                //! 可以使用icmp语句，value == 0 ? 1 : 0
                instr = new IcmpInstr(IRBuilder.getInstance().genVarName(), IcmpInstr.Op.eq, operand2, operand1);
                //扩展为32位
                instr = new ZextInstr(IRBuilder.getInstance().genVarName(), instr, BaseType.INT32);
                return instr;
            }
        } else {
            //函数调用
            Token indent = ((TokenNode) children.get(0)).getToken();
            String name = indent.getTokenContent();
            if(name.equals("getint")) {
                //如果是getint函数，要调用IOIInstr
//                IOInstr.GetInt instr = new IOInstr.GetInt(IRBuilder.getInstance().genVarName());
//                return instr;
                FuncSymbol funcSymbol = (FuncSymbol) SymbolManager.getManager().getSymbolByName(name);
                Function function = funcSymbol.getLLvmValue();
                ArrayList<Value> params = new ArrayList<>();
                if(children.get(2) instanceof FuncRParams) {
                    for(Node child : children.get(2).getChildren()) {
                        if(child instanceof Exp) {
                            params.add(child.genIR());
                        }
                    }
                }
                Instr instr = new CallInstr(IRBuilder.getInstance().genVarName(), function, params);
                return instr;
            } else {
                FuncSymbol funcSymbol = (FuncSymbol) SymbolManager.getManager().getSymbolByName(name);
                Function function = funcSymbol.getLLvmValue();
                ArrayList<Value> params = new ArrayList<>();
                if(children.get(2) instanceof FuncRParams) {
                    for(Node child : children.get(2).getChildren()) {
                        if(child instanceof Exp) {
                            params.add(child.genIR());
                        }
                    }
                }
                Instr instr = new CallInstr(IRBuilder.getInstance().genVarName(), function, params);
                return instr;
            }
        }
    }
}
