package llvm_ir;

public class Loop {
    //条件判断块
    private BasicBlock condBlock;
    //循环块
    private BasicBlock loopBlock;
    //后续块
    private BasicBlock followBlock;

    public Loop(BasicBlock condBlock, BasicBlock loopBlock, BasicBlock followBlock) {
        this.condBlock = condBlock;
        this.loopBlock = loopBlock;
        this.followBlock = followBlock;
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
}
