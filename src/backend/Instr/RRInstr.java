package backend.Instr;

import backend.Register;

//add rd,rs,rt
//R[rd] <- R[rs] + R[rt]
public class RRInstr extends MipsInstr{
    public enum Op {
        add,and,div,mult,or,slt,sub,xor,sgt,sltu, sge, sle;
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

    public RRInstr(Op op, Register rs, Register rt) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        if(op == Op.mult || op == Op.div) {
            return "\t" + op + " " + rs + ", " + rt + "\n";
        } else {
            return "\t" + op + " " + rd + ", " + rs + ", " + rt + "\n";
        }
    }
}
