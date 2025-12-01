package backend.Instr;

import backend.MipsBuilder;
import backend.Register;

public class MipsBranchInstr extends MipsInstr{
    public enum Op{
        beq,bgez,bgtz,blez,bltz,bne;
    }

    private Op op;
    private Register rs;
    private Register rt;
    private String label;

    public MipsBranchInstr(Op op, Register rs, Register rt, String label) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.label = label;
    }

    public MipsBranchInstr(Op op, Register rs, String label) {
        this.op = op;
        this.rs = rs;
        this.label = label;
    }

    @Override
    public String toString() {
        if(this.op == Op.beq || this.op == Op.bne) {
            return "\t" + op + " " + rs + ", " + rt + ", " + label + "\n";
        } else {
            return "\t" + op + " " + rs + ", " +  label + "\n";
        }
    }
}
