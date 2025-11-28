package backend.Instr;

import backend.Register;

public class MipsJumpInstr extends MipsInstr{
    public enum Op{
        j,jal,jr;
    }

    private Op op;
    private String target;
    private Register rd;

    public MipsJumpInstr(Op op, String target) {
        this.op = op;
        this.target = target;
    }

    public MipsJumpInstr(Op op, Register rd) {
        this.op = op;
        this.rd = rd;
    }

    @Override
    public String toString() {
        if(this.op == Op.jr) {
            return "jr " + rd;
        } else {
            return op + " " + target;
        }
    }
}
