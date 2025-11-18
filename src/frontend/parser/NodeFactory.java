package frontend.parser;

import frontend.*;
import frontend.Ast.Exp.*;
import frontend.Ast.Exp.Number;
import frontend.Ast.Func.*;
import frontend.Ast.Stmt.*;
import frontend.Ast.Var.*;
import utils.IOhandler;

import java.util.ArrayList;

public class NodeFactory {
    public static Node createNode(int startLine, int endLine, SyntaxVarType type, ArrayList<Node> children) {
        IOhandler.printSyntaxVarType(type);
        switch (type) {
            case COMP_UNIT:             return new CompUnit(startLine, endLine, type, children);
            case MAIN_FUNC_DEF:         return new MainFuncDef(startLine, endLine, type, children);

            case CONST_DECL:            return new ConstDecl(startLine, endLine, type, children);
            case CONST_DEF:             return new ConstDef(startLine, endLine, type, children);
            case CONST_INITVAL:         return new ConstInitVal(startLine, endLine, type, children);

            case VAR_DECL:              return new VarDecl(startLine, endLine, type, children);
            case VAR_DEF:               return new VarDef(startLine, endLine, type, children);
            case INIT_VAL:              return new InitVal(startLine, endLine, type, children);

            case FUNC_DEF:              return new FuncDef(startLine, endLine, type, children);
            case FUNC_TYPE:             return new FuncType(startLine, endLine, type, children);
            case FUNC_FORMAL_PARAMS:    return new FuncFParams(startLine, endLine, type, children);
            case FUNC_FORMAL_PARAM:     return new FuncFParam(startLine, endLine, type, children);
            case FUNC_REAL_PARAMS:      return new FuncRParams(startLine, endLine, type, children);
            case BLOCK:                 return new Block(startLine, endLine, type, children);

            case STMT:                  return new Stmt(startLine, endLine, type, children);
            case ASSIGN_STMT:           return new AssignStmt(startLine, endLine, type, children);
            case EXP_STMT:              return new ExpStmt(startLine, endLine, type, children);
            case BLOCK_STMT:            return new BlockStmt(startLine, endLine, type, children);
            case IF_STMT:               return new IfStmt(startLine, endLine, type, children);
            case FOR_STMT_WHOLE:        return new ForStmtWhole(startLine, endLine, type, children);
            case FOR_STMT:              return new ForStmt(startLine, endLine, type, children);
            case BREAK_STMT:            return new BreakStmt(startLine, endLine, type, children);
            case CONTINUE_STMT:         return new ContinueStmt(startLine, endLine, type, children);
            case RETURN_STMT:           return new ReturnStmt(startLine, endLine, type, children);
            case PRINTF_STMT:           return new PrintfStmt(startLine, endLine, type, children);

            case LVAL_EXP:              return new LVal(startLine, endLine, type, children);
            case PRIMARY_EXP:           return new PrimaryExp(startLine, endLine, type, children);
            case UNARY_EXP:             return new UnaryExp(startLine, endLine, type, children);
            case MUL_EXP:               return new MulExp(startLine, endLine, type, children);
            case ADD_EXP:               return new AddExp(startLine, endLine, type, children);
            case REL_EXP:               return new RelExp(startLine, endLine, type, children);
            case EQ_EXP:                return new EqExp(startLine, endLine, type, children);
            case LAND_EXP:              return new LAndExp(startLine, endLine, type, children);
            case LOR_EXP:               return new LOrExp(startLine, endLine, type, children);
            case EXP:                   return new Exp(startLine, endLine, type, children);
            case COND_EXP:              return new CondExp(startLine, endLine, type, children);
            case CONST_EXP:             return new ConstExp(startLine, endLine, type, children);

            case NUMBER:                return new Number(startLine, endLine, type, children);
            case UNARY_OP:              return new UnaryOp(startLine, endLine, type, children);
            default:                    return null;
        }
    }

    public static Node createNode(Token token) {
        IOhandler.printToken(token);
        return new TokenNode(token.getLineNum(), token.getLineNum(), SyntaxVarType.TOKEN, null, token);
    }
}
