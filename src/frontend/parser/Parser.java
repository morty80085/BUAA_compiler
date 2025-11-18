package frontend.parser;

import error.Error;
import error.ErrorRecorder;
import frontend.Token;
import frontend.TokenStream;
import frontend.*;
import utils.IOhandler;

import java.util.ArrayList;

public class Parser {
    private TokenStream tokenStream;
    private Token curentToken;

    public Parser(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
        read();
    }

    private void read() {
        curentToken = tokenStream.read();
    }

    private void unread() {
        tokenStream.unread();
    }

    public Node parseCompUnit() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        while(true) {
            if(curentToken == null || curentToken.getTokenType() == TokenType.EOF) {
                break;
            } else if(tokenStream.look(1).getTokenType() == TokenType.MAINTK) {
                //parse MainFuncDeF;
                node = parseMainFuncDef();
            } else if(tokenStream.look(2).getTokenType() == TokenType.LPARENT) {
                //parse FuncDef;
                node = parseFuncDef();
            } else if(curentToken.getTokenType() == TokenType.CONSTTK) {
                //parse ConstDecl;
                node = parseConstDecl();
            } else if(curentToken.getTokenType() == TokenType.INTTK || curentToken.getTokenType() == TokenType.STATICTK) {
                //parse VarDecl;
                node = parseVarDecl();
            } else {
                break;
            }
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.COMP_UNIT, children);
    }

    public Node parseConstDecl() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse "const" "int";
        for(int i = 0; i < 2; i++) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        }
        //parse ConstDef;
        node = parseConstDef();
        children.add(node);
        //parse { "," ConstDef};
        while(curentToken.getTokenType() == TokenType.COMMA) {
            //parse ",";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse ConstDef;
            node = parseConstDef();
            children.add(node);
        }
        //parse ";";
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i,node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.CONST_DECL, children);
    }

    public Node parseConstDef() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse Ident;
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse ["[" ConstExp "]"]
        if(curentToken.getTokenType() == TokenType.LBRACK) {
            //parse "[";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse ConstExp;
            node = parseConstExp();
            children.add(node);
            if(curentToken.getTokenType() == TokenType.RBRACK) {
                node = NodeFactory.createNode(curentToken);
                children.add(node);
                read();
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.k, node.getEndLine()));
            }
        }
        //parse "="
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse ConstInitVal;
        node = parseConstInitVal();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.CONST_DEF, children);
    }

    public Node parseConstInitVal() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        if(curentToken.getTokenType() == TokenType.LBRACE) {
            //parse "{"
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse [ConstExp {"," ConstExp}];
            if(curentToken.getTokenType() != TokenType.RBRACE) {
                //parse ConstExp;
                node = parseConstExp();
                children.add(node);
                while(curentToken.getTokenType() == TokenType.COMMA) {
                    //parse ",";
                    node = NodeFactory.createNode(curentToken);
                    children.add(node);
                    read();
                    //parse ConstExp;
                    node = parseConstExp();
                    children.add(node);
                }
            }
            //parse "}"
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            //parse ConstExp;
            node = parseConstExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.CONST_INITVAL, children);
    }

    public Node parseVarDecl() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse ["static"];
        if(curentToken.getTokenType() == TokenType.STATICTK) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        }
        //parse int;
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse VarDef;
        node = parseVarDef();
        children.add(node);
        //parse {"," VarDef};
        while(curentToken.getTokenType() == TokenType.COMMA) {
            //parse ","
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse VarDef;
            node = parseVarDef();
            children.add(node);
        }
        //parse ";"
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i, node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.VAR_DECL, children);
    }

    public Node parseVarDef() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse Ident;
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse ["[" ConstExp "]"]
        if(curentToken.getTokenType() == TokenType.LBRACK) {
            //parse "[";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse ConstExp;
            node = parseConstExp();
            children.add(node);
            //parse "]"
            if(curentToken.getTokenType() == TokenType.RBRACK) {
                node = NodeFactory.createNode(curentToken);
                children.add(node);
                read();
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.k, node.getEndLine()));
            }
        }
        //parse "=" InitVal;
        if(curentToken.getTokenType() == TokenType.ASSIGN) {
            //parse "=";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse InitVal;
            node = parseInitVal();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.VAR_DEF, children);
    }

    public Node parseInitVal() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        if(curentToken.getTokenType() == TokenType.LBRACE) {
            //parse "{"
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse [Exp {"," Exp}]
            if(curentToken.getTokenType() != TokenType.RBRACE) {
                //parse Exp;
                node = parseExp();
                children.add(node);
                //parse {"," Exp};
                while(curentToken.getTokenType() == TokenType.COMMA) {
                    //parse ","
                    node = NodeFactory.createNode(curentToken);
                    children.add(node);
                    read();
                    //parse Exp;
                    node = parseExp();
                    children.add(node);
                }
            }
            //parse "}"
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            //parse Exp
            node = parseExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.INIT_VAL, children);
    }

    public Node parseFuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse FuncType;
        node = parseFuncType();
        children.add(node);
        //parse Ident "(";
        for(int i = 0; i < 2; i++) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        }
        //parse [FuncFParams];
        if(curentToken.getTokenType() == TokenType.INTTK) {
            node = parseFuncFParams();
            children.add(node);
        }
        //parse ")";
        if(curentToken.getTokenType() == TokenType.RPARENT) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.j, node.getEndLine()));
        }
        //parse Block;
        node = parseBlock();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.FUNC_DEF, children);
    }

    public Node parseMainFuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse "int","main","(";
        for(int i = 0; i < 3; i++) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        }
        //parse ")";
        if(curentToken.getTokenType() == TokenType.RPARENT) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.j, node.getEndLine()));
        }
        //parse Block;
        node = parseBlock();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        //System.out.println(tokenStream.look(-1).getTokenContent());
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.MAIN_FUNC_DEF, children);
    }

    public Node parseFuncType() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse "void | int";
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.FUNC_TYPE, children);
    }

    public Node parseFuncFParams() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse FuncFParam
        Node node = parseFuncFParam();
        children.add(node);
        //parse {"," FuncFParam}
        while(curentToken.getTokenType() == TokenType.COMMA) {
            //parse ","
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse FuncFParam;
            node = parseFuncFParam();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.FUNC_FORMAL_PARAMS, children);
    }

    public Node parseFuncFParam() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse "int"
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse Ident;
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse {"[","]"};
        if(curentToken.getTokenType() == TokenType.LBRACK) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            if(curentToken.getTokenType() == TokenType.RBRACK) {
                node = NodeFactory.createNode(curentToken);
                children.add(node);
                read();
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.k, node.getEndLine()));
            }
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.FUNC_FORMAL_PARAM, children);
    }

    public Node parseBlock() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse "{";
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse {ConstDecl|VarDecl|Stmt}
        while(curentToken.getTokenType() != TokenType.RBRACE) {
            if(curentToken.getTokenType() == TokenType.CONSTTK) {
                //parse ConstDecl;
                node = parseConstDecl();
            } else if(curentToken.getTokenType() == TokenType.STATICTK || curentToken.getTokenType() == TokenType.INTTK) {
                //parse VarDecl;
                node = parseVarDecl();
            } else {
                //parse Stmt;
                node = parseStmt();
            }
            children.add(node);
        }
        //parse "}";
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.BLOCK, children);
    }

    public Node parseStmt() {
        if(curentToken.getTokenType() == TokenType.LBRACE) {
            return parseBlockStmt();
        } else if(curentToken.getTokenType() == TokenType.IFTK) {
            return parseIfStmt();
        } else if(curentToken.getTokenType() == TokenType.FORTK) {
            return parseForStmtWhole();
        } else if(curentToken.getTokenType() == TokenType.BREAKTK) {
            return parseBreakStmt();
        } else if(curentToken.getTokenType() == TokenType.CONTINUETK) {
            return parseContinueStmt();
        } else if(curentToken.getTokenType() == TokenType.RETURNTK) {
            return parseReturnStmt();
        } else if(curentToken.getTokenType() == TokenType.PRINTFTK) {
            return parsePrintfStmt();
        } else if(curentToken.getTokenType() == TokenType.SEMICN) {
            return parseExpStmt();
        }
        tokenStream.setBackTrackPoint();
        IOhandler.onOff = false;
        ErrorRecorder.onOff = false;
        parseExp();
        IOhandler.onOff = true;
        ErrorRecorder.onOff = true;
        if(curentToken.getTokenType() ==TokenType.ASSIGN) {
            curentToken = tokenStream.setBack();
            return parseAssignStmt();
        } else {
            curentToken = tokenStream.setBack();
            return parseExpStmt();
        }
    }

    public Node parseAssignStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse LVal;
        node = parseLValExp();
        children.add(node);
        //parse "=";
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse Exp;
        node = parseExp();
        children.add(node);
        //parse ";"
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i, node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.ASSIGN_STMT, children);
    }

    public Node parseExpStmt() {
        ArrayList<Node> children =new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse [Exp];
        if(curentToken.getTokenType() == TokenType.PLUS || curentToken.getTokenType() == TokenType.MINU
                || curentToken.getTokenType() == TokenType.NOT || curentToken.getTokenType() == TokenType.IDENFR
                || curentToken.getTokenType() == TokenType.LPARENT || curentToken.getTokenType() == TokenType.INTCON) {
            node = parseExp();
            children.add(node);
        }
        //parse ";"
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i, node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.EXP_STMT, children);
    }

    public Node parseBlockStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse Block
        Node node = parseBlock();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.BLOCK_STMT, children);
    }

    public Node parseIfStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse "if", "(";
        for(int i = 0; i < 2; i++) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        }
        //parse Cond;
        node = parseCond();
        children.add(node);
        //parse ")"
        if(curentToken.getTokenType() == TokenType.RPARENT) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.j, node.getEndLine()));
        }
        //parse Stmt;
        node = parseStmt();
        children.add(node);
        //parse {"else" Stmt}
        if(curentToken.getTokenType() == TokenType.ELSETK) {
            //parse else;
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse Stmt;
            node = parseStmt();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.IF_STMT, children);
    }

    public Node parseForStmtWhole() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse "for", "(";
        for(int i = 0; i < 2; i++) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        }
        //parse [ForStmt];
        if(curentToken.getTokenType() != TokenType.SEMICN) {
            node = parseForStmt();
            children.add(node);
        }
        //parse ";"
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse [Cond];
        if(curentToken.getTokenType() != TokenType.SEMICN) {
            node = parseCond();
            children.add(node);
        }
        //parse ";";
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse [ForStmt];
        if(curentToken.getTokenType() != TokenType.RPARENT) {
            node = parseForStmt();
            children.add(node);
        }
        //parse ")";
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse Stmt;
        node = parseStmt();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.FOR_STMT_WHOLE, children);
    }

    public Node parseBreakStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse "break";
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse ";";
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i, node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.BREAK_STMT, children);
    }

    public Node parseContinueStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse "continue";
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse ";";
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i, node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.CONTINUE_STMT, children);
    }

    public Node parseReturnStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse "return";
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse [Exp];
        if(curentToken.getTokenType() == TokenType.LPARENT || curentToken.getTokenType() == TokenType.INTCON
                || curentToken.getTokenType() == TokenType.IDENFR || curentToken.getTokenType() == TokenType.PLUS
                || curentToken.getTokenType() == TokenType.MINU || curentToken.getTokenType() == TokenType.NOT) {
            node = parseExp();
            children.add(node);
        }
        //parse ";";
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i, node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.RETURN_STMT, children);
    }

    public Node parsePrintfStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        //parse "printf","(","string";
        for(int i = 0; i < 3; i++) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        }
        //parse {",", Exp};
        while(curentToken.getTokenType() == TokenType.COMMA) {
            //parse ",";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse Exp;
            node = parseExp();
            children.add(node);
        }
        //parse ")";
        if(curentToken.getTokenType() == TokenType.RPARENT) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.j, node.getEndLine()));
        }
        //parse ";";
        if(curentToken.getTokenType() == TokenType.SEMICN) {
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
        } else {
            ErrorRecorder.addError(new Error(Error.ErrorType.i, node.getEndLine()));
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.PRINTF_STMT, children);
    }

    public Node parseForStmt() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse LVal;
        Node node = parseLValExp();
        children.add(node);
        //parse "=";
        node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse Exp;
        node = parseExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.COMMA) {
            //parse ",";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse LVal;
            node = parseLValExp();
            children.add(node);
            //parse "=";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse Exp;
            node = parseExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.FOR_STMT, children);
    }

    public Node parseExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseAddExp();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.EXP, children);
    }

    public Node parseCond() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseLOrExp();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.COND_EXP, children);
    }

    public Node parseLValExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        //parse Ident;
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        //parse ["Exp"]
        if(curentToken.getTokenType() == TokenType.LBRACK) {
            //parse "["
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse "Exp"
            node = parseExp();
            children.add(node);
            //parse "]"
            if(curentToken.getTokenType() == TokenType.RBRACK) {
                node = NodeFactory.createNode(curentToken);
                children.add(node);
                read();
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.k,node.getEndLine()));
            }
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.LVAL_EXP, children);
    }

    public Node parsePrimaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        if(curentToken.getTokenType() == TokenType.INTCON) {
            //parse Number
            node = parseNumber();
            children.add(node);
        } else if(curentToken.getTokenType() == TokenType.LPARENT) {
            //parse "(";
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse Exp;
            node = parseExp();
            children.add(node);
            //parse ")";
            if(curentToken.getTokenType() == TokenType.RPARENT) {
                node = NodeFactory.createNode(curentToken);
                children.add(node);
                read();
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.j, node.getEndLine()));
            }
        } else {
            node = parseLValExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.PRIMARY_EXP, children);
    }

    public Node parseNumber() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.NUMBER, children);
    }

    public Node parseUnaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = null;
        if(curentToken.getTokenType() == TokenType.IDENFR && tokenStream.look(1).getTokenType() == TokenType.LPARENT) {
            //parse ident;
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse "("
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //parse [FuncRParams];
            if(curentToken.getTokenType() == TokenType.LPARENT || curentToken.getTokenType() == TokenType.INTCON
                    || curentToken.getTokenType() == TokenType.IDENFR || curentToken.getTokenType() == TokenType.PLUS
                    || curentToken.getTokenType() == TokenType.MINU || curentToken.getTokenType() == TokenType.NOT) {
                node = parseFuncRParams();
                children.add(node);
            }
            //parse ")"
            if(curentToken.getTokenType() == TokenType.RPARENT) {
                node = NodeFactory.createNode(curentToken);
                children.add(node);
                read();
            } else {
                ErrorRecorder.addError(new Error(Error.ErrorType.j, node.getEndLine()));
            }
        } else if(curentToken.getTokenType() == TokenType.PLUS || curentToken.getTokenType() == TokenType.MINU
                || curentToken.getTokenType() == TokenType.NOT) {
            //parse "+","-","!";
            node = parseUnaryOp();
            children.add(node);
            //parse UnaryExp;
            node = parseUnaryExp();
            children.add(node);
        } else {
            node = parsePrimaryExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.UNARY_EXP, children);
    }

    public Node parseUnaryOp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = NodeFactory.createNode(curentToken);
        children.add(node);
        read();
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.UNARY_OP, children);
    }

    public Node parseFuncRParams() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.COMMA) {
            //处理“,”;
            NodeFactory.createNode(curentToken);
            read();
            //递归处理Exp;
            node = parseExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.FUNC_REAL_PARAMS, children);
    }

    public Node parseMulExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseUnaryExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.MULT || curentToken.getTokenType() == TokenType.DIV
                || curentToken.getTokenType() == TokenType.MOD) {
            IOhandler.printSyntaxVarType(SyntaxVarType.MUL_EXP);
            //处理“*”，“/”，“%”;
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //递归处理UnaryExp;
            node = parseUnaryExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.MUL_EXP, children);
    }

    public Node parseAddExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseMulExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.PLUS || curentToken.getTokenType() == TokenType.MINU) {
            IOhandler.printSyntaxVarType(SyntaxVarType.ADD_EXP);
            //处理“+”，“-”;
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //递归处理MulExp;
            node = parseMulExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.ADD_EXP, children);
    }

    public Node parseRelExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseAddExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.LSS || curentToken.getTokenType() == TokenType.LEQ
                || curentToken.getTokenType() == TokenType.GRE || curentToken.getTokenType() == TokenType.GEQ) {
            IOhandler.printSyntaxVarType(SyntaxVarType.REL_EXP);
            //处理“<”,“<=”,“>”,“>=”;
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //递归处理AddExp;
            node = parseAddExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.REL_EXP, children);
    }

    public Node parseEqExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseRelExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.EQL || curentToken.getTokenType() == TokenType.NEQ) {
            IOhandler.printSyntaxVarType(SyntaxVarType.EQ_EXP);
            //处理“==”，“!=”;
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //递归处理RelExp;
            node = parseRelExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.EQ_EXP, children);
    }

    public Node parseLAndExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseEqExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.AND) {
            IOhandler.printSyntaxVarType(SyntaxVarType.LAND_EXP);
            //处理“&&”；
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //递归处理EqExp;
            node = parseEqExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.LAND_EXP, children);
    }

    public Node parseLOrExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseLAndExp();
        children.add(node);
        while(curentToken.getTokenType() == TokenType.OR) {
            IOhandler.printSyntaxVarType(SyntaxVarType.LOR_EXP);
            //处理“||”;
            node = NodeFactory.createNode(curentToken);
            children.add(node);
            read();
            //递归处理LAndExp;
            node = parseLAndExp();
            children.add(node);
        }
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.LOR_EXP, children);
    }

    public Node parseConstExp() {
        ArrayList<Node> children = new ArrayList<>();
        int startLine = curentToken.getLineNum();
        Node node = parseAddExp();
        children.add(node);
        int endLine = tokenStream.look(-1).getLineNum();
        return NodeFactory.createNode(startLine, endLine, SyntaxVarType.CONST_EXP, children);
    }
}
