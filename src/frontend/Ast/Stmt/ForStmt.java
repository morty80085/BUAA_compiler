package frontend.Ast.Stmt;

import error.Error;
import error.ErrorRecorder;
import frontend.Ast.Exp.LVal;
import frontend.Node;
import frontend.SyntaxVarType;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.StoreInstr;

import java.util.ArrayList;
//ForStmt → LVal '=' Exp { ',' LVal '=' Exp }
public class ForStmt extends Node{
    public ArrayList<LVal> lValArrayList = new ArrayList<>();

    public ForStmt(int startLine, int endLine, SyntaxVarType syntaxVarType, ArrayList<Node> children) {
        super(startLine,endLine,syntaxVarType,children);
        for(Node child : children) {
            if(child instanceof LVal) {
                lValArrayList.add((LVal) child);
            }
        }
    }

    @Override
    public void visit() {
        for(LVal child : lValArrayList) {
            if(child.isConst()) {
                ErrorRecorder.addError(new Error(Error.ErrorType.h,startLine));
            }
        }
        super.visit();
    }

    public ArrayList<Value> genIRList() {
        ArrayList<Value> valueList = new ArrayList<>();
        for(int i = 0; i < children.size(); i++) {
            if(children.get(i) instanceof LVal) {
                Value lval = ((LVal) children.get(i)).genIRForAssign();
                Value exp = children.get(i + 2).genIR();
                Instr instr = new StoreInstr(IRBuilder.getInstance().genVarName(), exp, lval);
                valueList.add(instr);
            }
        }
        return valueList;
    }
}
