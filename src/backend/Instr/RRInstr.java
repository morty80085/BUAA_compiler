package backend.Instr;

import backend.Register;

//add rd,rs,rt
//R[rd] <- R[rs] + R[rt]
public class RRInstr extends MipsInstr{
    public enum Op {
        add,and,div,mult,or,slt,sub;
    }

    private Op op;
    private Register rd;
    private Register rs;
    private Register rt;

    public RRInstr(Op op, Register rd, Register rs, Register rt) {
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return "\t" + op + " " + rd + ", " + rs + ", " + rt;
    }
}
