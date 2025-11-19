package llvm_ir;

public class Loop {
    //条件判断块
    private BasicBlock condBlock;
    //循环块
    private BasicBlock loopBlock;
    //后续块
    private BasicBlock followBlock;
    //forStmt1
    private BasicBlock forStmt1;
    //forStmt2
    private BasicBlock forStmt2;

    public Loop(BasicBlock condBlock, BasicBlock loopBlock, BasicBlock followBlock, BasicBlock forStmt1, BasicBlock forStmt2) {
        this.condBlock = condBlock;
        this.loopBlock = loopBlock;
        this.followBlock = followBlock;
        this.forStmt1 = forStmt1;
        this.forStmt2 = forStmt2;
    }

    public BasicBlock getCondBlock() {
        return this.condBlock;
    }

    public BasicBlock getLoopBlock() {
        return this.loopBlock;
    }

    public BasicBlock getFollowBlock() {
        return this.followBlock;
    }
    public BasicBlock getForStmt1() {
        return this.forStmt1;
    }

    public BasicBlock getForStmt2() {
        return this.forStmt2;
    }
}
