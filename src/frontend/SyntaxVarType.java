package frontend;

public enum SyntaxVarType {
    COMP_UNIT("CompUnit"),
    MAIN_FUNC_DEF("MainFuncDef"),

    //const
    CONST_DECL("ConstDecl"),
    CONST_DEF("ConstDef"),
    CONST_INITVAL("ConstInitVal"),

    //var
    VAR_DECL("VarDecl"),
    VAR_DEF("VarDef"),
    INIT_VAL("InitVal"),

    //func
    FUNC_DEF("FuncDef"),
    FUNC_TYPE("FuncType"),
    FUNC_FORMAL_PARAMS("FuncFParams"),
    FUNC_FORMAL_PARAM("FuncFParam"),
    FUNC_REAL_PARAMS("FuncRParams"),
    BLOCK("Block"),

    //stmt
    STMT("Stmt"),
    ASSIGN_STMT("Stmt"),
    EXP_STMT("Stmt"),
    BLOCK_STMT("Stmt"),
    IF_STMT("Stmt"),
    FOR_STMT_WHOLE("Stmt"),
    FOR_STMT("ForStmt"),
    BREAK_STMT("Stmt"),
    CONTINUE_STMT("Stmt"),
    RETURN_STMT("Stmt"),
    PRINTF_STMT("Stmt"),

    //exp
    LVAL_EXP("LVal"),
    PRIMARY_EXP("PrimaryExp"),
    UNARY_EXP("UnaryExp"),
    MUL_EXP("MulExp"),
    ADD_EXP("AddExp"),
    REL_EXP("RelExp"),
    EQ_EXP("EqExp"),
    LAND_EXP("LAndExp"),
    LOR_EXP("LOrExp"),
    EXP("Exp"),
    COND_EXP("Cond"),
    CONST_EXP("ConstExp"),

    NUMBER("Number"),
    UNARY_OP("UnaryOp"),

    TOKEN("token"),
    ;

    private final String typeName;

    private SyntaxVarType(String typeName) {
        this.typeName = typeName;
    }

    public String toString() {
        return this.typeName;
    }
}
