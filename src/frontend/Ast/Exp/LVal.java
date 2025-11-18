package frontend.Ast.Exp;

import error.Error;
import error.ErrorRecorder;
import frontend.Node;
import frontend.SyntaxVarType;
import frontend.TokenNode;
import frontend.TokenType;
import frontend.symbol.*;
import llvm_ir.Constant;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.GepInstr;
import llvm_ir.instr.LoadInstr;

import java.util.ArrayList;

//LVal → Ident ['[' Exp ']']
public class LVal extends Node{
    public LVal(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine, endLine, syntaxVarType, children);
    }

    public boolean isConst() {
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        Symbol symbol = SymbolManager.getManager().getSymbolByName(name);
        if(symbol instanceof ConstSymbol) {
            return true;
        }
        return false;
    }

    @Override
    public Integer getDim() {
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        Symbol symbol = SymbolManager.getManager().getSymbolByName(name);
        if(symbol == null) {
            return null;
        }
        int dim = 0;
        int cnt = 0;
        if(symbol instanceof ConstSymbol && ((ConstSymbol) symbol).isArray()) {
            dim = 1;
        } else if(symbol instanceof VarSymbol && ((VarSymbol) symbol).isArray()) {
            dim = 1;
        } else if(symbol instanceof StaticSymbol && ((StaticSymbol) symbol).isArray()) {
            dim = 1;
        }
        for (Node child : children) {
            if (child instanceof TokenNode && ((TokenNode)child).getToken().getTokenType() == TokenType.LBRACK) {
                cnt++;
            }
        }
        return dim - cnt;
    }


    @Override
    public int execute() {
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        Symbol symbol = SymbolManager.getManager().getSymbolByName(name);
        //防止出错
        if(symbol == null) {
            return 0;
        }

        int len = 0;
        for(Node child: children) {
            if(child instanceof Exp) {
                len = child.execute();
            }
        }

        if(symbol instanceof ConstSymbol) {
            //ConstSymbol
            ConstSymbol constSymbol = (ConstSymbol) symbol;
            if(constSymbol.isArray()) {
                return constSymbol.getIntValue(len);
            } else {
                return constSymbol.getIntValue();
            }
        } else if(symbol instanceof StaticSymbol) {
            //StaticSymbol
            StaticSymbol staticSymbol = (StaticSymbol) symbol;
            if(staticSymbol.isArray()) {
                return staticSymbol.getIntValue(len);
            } else {
                return staticSymbol.getIntValue();
            }
        } else {
            //VarSymbol
            VarSymbol varSymbol = (VarSymbol) symbol;
            if(varSymbol.isArray()) {
                return varSymbol.getIntValue(len);
            } else {
                return varSymbol.getIntValue();
            }
        }
    }

    @Override
    public void visit() {
        //Error c
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        if(SymbolManager.getManager().getSymbolByName(name) == null) {
            ErrorRecorder.addError(new Error(Error.ErrorType.c, children.get(0).getEndLine()));
        }
        super.visit();
    }

    //用于生成在等号右边的LVal的语句
    public Value genIRForValue() {
        //num和expList用于查看是否是int[],即函数实参中的地址类型
        int num = 0;
        ArrayList<Value> expList = new ArrayList<>();
        for(Node child :children) {
            if(child instanceof Exp) {
                expList.add(child.genIR());
                num = num + 1;
            }
        }
        //获取Symbol
        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        Symbol symbol = SymbolManager.getManager().getSymbolByName(name);
        Instr instr = null;
        if(symbol instanceof ConstSymbol) {
            ConstSymbol constSymbol = (ConstSymbol) symbol;
            if(constSymbol.isArray()) {
                if(num == 0) {
                    //是一个地址，用于传参
                    instr = new GepInstr(IRBuilder.getInstance().genVarName(), constSymbol.getlLvmValue(),new Constant(0));
                    return instr;
                } else {
                    //是一个数
                    instr = new GepInstr(IRBuilder.getInstance().genVarName(), constSymbol.getlLvmValue(),expList.get(0));
                    instr = new LoadInstr(IRBuilder.getInstance().genVarName(), instr);
                    return instr;
                }
            } else {
                instr = new LoadInstr(IRBuilder.getInstance().genVarName(), constSymbol.getlLvmValue());
                return instr;
            }
        } else if(symbol instanceof StaticSymbol) {
            StaticSymbol staticSymbol = (StaticSymbol) symbol;
            if(staticSymbol.isArray()) {
                if(num == 0) {
                    //是一个地址，用于传参
                    instr = new GepInstr(IRBuilder.getInstance().genVarName(), staticSymbol.getlLvmValue(),new Constant(0));
                    return instr;
                } else {
                    //是一个数
                    instr = new GepInstr(IRBuilder.getInstance().genVarName(), staticSymbol.getlLvmValue(),expList.get(0));
                    instr = new LoadInstr(IRBuilder.getInstance().genVarName(), instr);
                    return instr;
                }
            } else {
                instr = new LoadInstr(IRBuilder.getInstance().genVarName(), staticSymbol.getlLvmValue());
                return instr;
            }
        } else if(symbol instanceof VarSymbol) {
            VarSymbol varSymbol = (VarSymbol) symbol;
            if(varSymbol.isArray()) {
                if(num == 0) {
                    //是一个地址，用于传参
                    instr = new GepInstr(IRBuilder.getInstance().genVarName(), varSymbol.getlLvmValue(),new Constant(0));
                    return instr;
                } else {
                    //是一个数
                    instr = new GepInstr(IRBuilder.getInstance().genVarName(), varSymbol.getlLvmValue(),expList.get(0));
                    instr = new LoadInstr(IRBuilder.getInstance().genVarName(), instr);
                    return instr;
                }
            } else {
                instr = new LoadInstr(IRBuilder.getInstance().genVarName(), varSymbol.getlLvmValue());
                return instr;
            }
        }
        return null;
    }

    //用于生成在等号左边的LVal的语句(即生成一个地址)
    public Value genIRForAssign() {
        //此时的symbol只有可能是static或var
        Value exp = null;
        for(Node child: children) {
            if(child instanceof Exp) {
                exp = child.genIR();
            }
        }

        String name = ((TokenNode) children.get(0)).getToken().getTokenContent();
        Symbol symbol = SymbolManager.getManager().getSymbolByName(name);
        Instr instr = null;

        if(symbol instanceof VarSymbol) {
            VarSymbol varSymbol = (VarSymbol) symbol;
            if(varSymbol.isArray()) {
                instr = new GepInstr(IRBuilder.getInstance().genVarName(), varSymbol.getlLvmValue(), exp);
                return instr;
            } else {
                return varSymbol.getlLvmValue();
            }
        } else if(symbol instanceof StaticSymbol) {
            StaticSymbol staticSymbol = (StaticSymbol) symbol;
            if(staticSymbol.isArray()) {
                instr = new GepInstr(IRBuilder.getInstance().genVarName(), staticSymbol.getlLvmValue(), exp);
                return instr;
            } else {
                return staticSymbol.getlLvmValue();
            }
        }

        return null;
    }
}
